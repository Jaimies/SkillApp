package com.jdevs.timeo.data.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentChange.Type.ADDED
import com.google.firebase.firestore.DocumentChange.Type.MODIFIED
import com.google.firebase.firestore.DocumentChange.Type.REMOVED
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Operation.ChangeType.Added
import com.jdevs.timeo.domain.model.Operation.ChangeType.Modified
import com.jdevs.timeo.domain.model.Operation.ChangeType.Removed
import com.jdevs.timeo.domain.model.Operation.Changed
import com.jdevs.timeo.domain.model.Operation.LastItemReached
import com.jdevs.timeo.domain.model.Operation.Successful

class ListLiveData<T : Any, O : Any>(
    private val query: Query,
    private val setLastDocument: (DocumentSnapshot) -> Unit,
    private val onLastItemReached: () -> Unit,
    private val type: Class<T>,
    private val mapFunction: (T) -> O,
    private val pageSize: Long
) : FirestoreListenerLiveData<Operation<O>>(),
    EventListener<QuerySnapshot> {

    override fun registerListener() = query.addSnapshotListener(this)

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {

        if (exception != null || querySnapshot == null) {
            Log.w(TAG, "Firestore data update failed", exception)
            return
        }

        querySnapshot.documentChanges.forEach(::processDocumentChange)
        value = Successful()

        if (querySnapshot.size() < pageSize) {
            value = LastItemReached()
            onLastItemReached()
        } else {
            setLastDocument(querySnapshot.documents.last())
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processDocumentChange(documentChange: DocumentChange) {

        val item = try {
            documentChange.document.toObject(type)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return
        }

        val changeType = when (documentChange.type) {
            ADDED -> Added
            MODIFIED -> Modified
            REMOVED -> Removed
        }

        value = Changed(mapFunction(item), changeType)
    }

    companion object {
        const val TAG = "ListLiveData"
    }
}
