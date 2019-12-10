package com.jdevs.timeo.data.livedata

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.data.Operation
import com.jdevs.timeo.util.OperationStates
import com.jdevs.timeo.util.OperationStates.FAILED

abstract class ItemListLiveData(
    private var query: Query?,
    private val setLastVisibleItem: (DocumentSnapshot) -> Unit,
    private val onLastItemReached: () -> Unit
) : LiveData<Operation>(), EventListener<QuerySnapshot> {

    protected abstract val dataType: Class<*>
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

            value = Operation(exception = exception, type = FAILED)
            return
        }

        for (documentChange in querySnapshot.documentChanges) {

            processDocumentChange(documentChange)
        }

        value = Operation(type = OperationStates.FINISHED)

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
                Operation(activity, type = OperationStates.ADDED, id = documentId)

            DocumentChange.Type.MODIFIED ->
                Operation(activity, type = OperationStates.MODIFIED, id = documentId)

            DocumentChange.Type.REMOVED ->
                Operation(activity, type = OperationStates.REMOVED, id = documentId)
        }

        value = operation
    }
}
