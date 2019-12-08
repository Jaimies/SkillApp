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
import com.jdevs.timeo.data.OperationOrException
import com.jdevs.timeo.util.OperationConstants
import com.jdevs.timeo.util.TAG

abstract class ItemListLiveData(
    private var query: Query?,
    private val setLastVisibleItem: (DocumentSnapshot) -> Unit,
    private val onLastItemReached: () -> Unit
) : LiveData<OperationOrException>(),
    EventListener<QuerySnapshot> {

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

            Log.w(TAG, "Failed to get data from Firestore", exception)
            return
        }

        for (documentChange in querySnapshot.documentChanges) {

            processDocumentChange(documentChange)
        }

        value =
            OperationOrException(type = OperationConstants.FINISHED)

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
                OperationOrException(
                    activity,
                    type = OperationConstants.ADDED,
                    id = documentId
                )

            DocumentChange.Type.MODIFIED ->
                OperationOrException(
                    activity,
                    type = OperationConstants.MODIFIED,
                    id = documentId
                )

            DocumentChange.Type.REMOVED ->
                OperationOrException(
                    activity,
                    type = OperationConstants.REMOVED,
                    id = documentId
                )
        }

        value = operation
    }
}
