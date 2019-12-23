package com.jdevs.timeo.data.source.remote

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Operation
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.OperationTypes.ADDED
import com.jdevs.timeo.util.OperationTypes.FAILED
import com.jdevs.timeo.util.OperationTypes.FINISHED
import com.jdevs.timeo.util.OperationTypes.MODIFIED
import com.jdevs.timeo.util.OperationTypes.REMOVED
import com.jdevs.timeo.util.RecordsConstants

sealed class ItemsLiveData(
    private var query: Query,
    private val setLastVisibleItem: (DocumentSnapshot) -> Unit = {},
    private val onLastItemReached: () -> Unit = {},
    private val type: Class<*>,
    private val fetchLimit: Long
) : LiveData<Operation>(), EventListener<QuerySnapshot> {

    private var listener: ListenerRegistration? = null

    override fun onActive() {

        listener = query.addSnapshotListener(this)
    }

    override fun onInactive() {

        listener?.remove()
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

        value = Operation(type = FINISHED)

        if (querySnapshot.size() < fetchLimit) {

            onLastItemReached()
        } else {

            setLastVisibleItem(querySnapshot.documents.last())
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processDocumentChange(documentChange: DocumentChange) {

        val activity = try {

            documentChange.document.toObject(type)
        } catch (e: RuntimeException) {

            e.printStackTrace()
            type.getConstructor().newInstance()
        }

        val docId = documentChange.document.id

        val operationType = when (documentChange.type) {
            DocumentChange.Type.ADDED -> ADDED
            DocumentChange.Type.MODIFIED -> MODIFIED
            DocumentChange.Type.REMOVED -> REMOVED
        }

        value = Operation(
            item = activity,
            type = operationType,
            id = docId
        )
    }

    class ActivitiesLiveData(
        query: Query,
        setLastVisibleItem: (DocumentSnapshot) -> Unit = {},
        onLastItemReached: () -> Unit = {}
    ) : ItemsLiveData(
        query,
        setLastVisibleItem,
        onLastItemReached,
        Activity::class.java,
        ActivitiesConstants.FETCH_LIMIT
    )

    class RecordsLiveData(
        query: Query,
        setLastVisibleItem: (DocumentSnapshot) -> Unit = {},
        onLastItemReached: () -> Unit = {}
    ) : ItemsLiveData(
        query,
        setLastVisibleItem,
        onLastItemReached,
        Record::class.java,
        RecordsConstants.FETCH_LIMIT
    )
}
