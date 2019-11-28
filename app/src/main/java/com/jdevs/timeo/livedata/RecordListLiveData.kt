package com.jdevs.timeo.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.R
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.operations.RecordOperation
import com.jdevs.timeo.util.RECORDS_FETCH_LIMIT
import com.jdevs.timeo.util.TAG

class RecordListLiveData(
    private val query: Query,
    private val setLastVisibleRecord: (DocumentSnapshot) -> Unit,
    private val onLastRecordReached: () -> Unit
) : LiveData<RecordOperation>(),
    EventListener<QuerySnapshot> {

    private var listenerRegistration: ListenerRegistration? = null
    private var wasLoaderHidden = false

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
        wasLoaderHidden = false
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

        if (!wasLoaderHidden) {

            value = RecordOperation(null, R.id.OPERATION_LOADED, "")
            wasLoaderHidden = true
        }

        for (documentChange in querySnapshot.documentChanges) {

            processDocumentChange(documentChange)
        }

        val querySnapshotSize = querySnapshot.size()

        if (querySnapshotSize < RECORDS_FETCH_LIMIT) {

            onLastRecordReached()
        } else {

            val lastVisibleProduct = querySnapshot.documents[querySnapshotSize - 1]

            setLastVisibleRecord(lastVisibleProduct)
        }
    }

    private fun processDocumentChange(documentChange: DocumentChange) {

        val record = try {

            documentChange.document.toObject(Record::class.java)
        } catch (e: RuntimeException) {

            e.printStackTrace()
            Record()
        }

        val documentId = documentChange.document.id

        val operation = when (documentChange.type) {

            DocumentChange.Type.ADDED ->
                RecordOperation(record, R.id.OPERATION_ADDED, documentId)

            DocumentChange.Type.MODIFIED ->
                RecordOperation(record, R.id.OPERATION_MODIFIED, documentId)

            DocumentChange.Type.REMOVED ->
                RecordOperation(record, R.id.OPERATION_REMOVED, documentId)
        }

        value = operation
    }
}
