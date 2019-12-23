package com.jdevs.timeo.data.source.remote

import androidx.lifecycle.LiveData
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
import com.jdevs.timeo.util.OperationTypes.FAILED
import com.jdevs.timeo.util.OperationTypes.LAST_ITEM_REACHED
import com.jdevs.timeo.util.OperationTypes.SUCCESSFUL
import com.jdevs.timeo.util.RecordsConstants

sealed class ItemsLiveData(
    private val query: Query,
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

        val data = querySnapshot.documentChanges.map { documentChange ->

            try {

                documentChange.document.toObject(type)
            } catch (e: RuntimeException) {

                e.printStackTrace()
                type.getConstructor().newInstance()
            }
        }

        value = Operation(data = data, type = SUCCESSFUL)

        if (querySnapshot.size() < fetchLimit) {

            value = Operation(type = LAST_ITEM_REACHED)
            onLastItemReached()
        } else {

            setLastVisibleItem(querySnapshot.documents.last())
        }
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
