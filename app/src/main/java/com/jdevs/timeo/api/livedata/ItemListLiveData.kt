package com.jdevs.timeo.api.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.data.operations.Operation
import com.jdevs.timeo.util.OperationConstants
import com.jdevs.timeo.util.TAG

abstract class ItemListLiveData(
    private var query: Query?,
    private val setLastVisibleItem: (DocumentSnapshot) -> Unit,
    private val onLastItemReached: () -> Unit
) : LiveData<Operation>(),
    EventListener<QuerySnapshot> {

    protected abstract val dataType: Class<*>
    protected abstract val operation: (Any?, Int, String) -> Operation
    protected abstract val fetchLimit: Long

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {

        listenerRegistration = query?.addSnapshotListener(this)
    }

    override fun onInactive() {

        listenerRegistration?.remove()
    }

    override fun onEvent(
        querySnapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {

        if (exception != null || querySnapshot == null) {

            Log.w(TAG, "Failed to get data from Firestore", exception)
            return
        }

        for (documentChange in querySnapshot.documentChanges) {

            processDocumentChange(documentChange)
        }

        value = operation(null, OperationConstants.FINISHED, "")

        if (querySnapshot.size() < fetchLimit) {

            onLastItemReached()
        } else {

            setLastVisibleItem(querySnapshot.documents.last())
        }
    }

    fun setQuery(newQuery: Query?) {

        query = newQuery?.also {

            it.addSnapshotListener(this)
        }
    }

    private fun processDocumentChange(documentChange: DocumentChange) {

        val activity = try {

            documentChange.document.toObject(dataType)
        } catch (e: RuntimeException) {

            e.printStackTrace()
            dataType.getConstructor().newInstance()
        }

        val documentId = documentChange.document.id

        val operation = when (documentChange.type) {

            DocumentChange.Type.ADDED ->
                operation(activity, OperationConstants.ADDED, documentId)

            DocumentChange.Type.MODIFIED ->
                operation(activity, OperationConstants.MODIFIED, documentId)

            DocumentChange.Type.REMOVED ->
                operation(activity, OperationConstants.REMOVED, documentId)
        }

        value = operation
    }
}
