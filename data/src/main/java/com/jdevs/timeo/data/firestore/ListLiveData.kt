package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.shared.OperationType.ADDED
import com.jdevs.timeo.shared.OperationType.FAILED
import com.jdevs.timeo.shared.OperationType.LAST_ITEM_REACHED
import com.jdevs.timeo.shared.OperationType.MODIFIED
import com.jdevs.timeo.shared.OperationType.REMOVED
import com.jdevs.timeo.shared.OperationType.SUCCESSFUL

class ListLiveData<T : Any, O : Any>(
    private val query: Query,
    private val setLastDocument: (DocumentSnapshot) -> Unit,
    private val onLastItemReached: () -> Unit,
    private val type: Class<T>,
    private val mapFunction: (T) -> O,
    private val pageSize: Long
) : FirestoreListenerLiveData<Operation<O>>(), EventListener<QuerySnapshot> {

    override fun registerListener() = query.addSnapshotListener(this)

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {

        if (exception != null || querySnapshot == null) {

            value = Operation(exception = exception, type = FAILED)
            return
        }

        querySnapshot.documentChanges.forEach(::processDocumentChange)
        value = Operation(type = SUCCESSFUL)

        if (querySnapshot.size() < pageSize) {
            value = Operation(type = LAST_ITEM_REACHED)
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

        val operationType = when (documentChange.type) {
            DocumentChange.Type.ADDED -> ADDED
            DocumentChange.Type.MODIFIED -> MODIFIED
            DocumentChange.Type.REMOVED -> REMOVED
        }

        value = Operation(mapFunction(item), type = operationType)
    }
}
