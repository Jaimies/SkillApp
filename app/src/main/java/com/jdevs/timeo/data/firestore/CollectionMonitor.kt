package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.FirestoreConstants
import com.jdevs.timeo.util.LiveDataConstructor

class CollectionMonitor(
    private val liveData: LiveDataConstructor,
    private val pageSize: Long,
    private val orderBy: String
) {

    private lateinit var query: Query
    private lateinit var ref: CollectionReference

    private var lastVisibleItem: DocumentSnapshot? = null
    private var isLastItemReached = false
    private val newQuery
        get() = ref
            .orderBy(orderBy, Query.Direction.DESCENDING)
            .limit(pageSize)

    fun setRef(ref: CollectionReference) {

        this.ref = ref
        reset()
    }

    fun reset() {

        if (!::ref.isInitialized) {

            return
        }

        lastVisibleItem = null
        isLastItemReached = false
        query = newQuery
    }

    fun getLiveData(): ItemsLiveData? {

        if (isLastItemReached) {

            return null
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return liveData(query, { lastVisibleItem = it }) { isLastItemReached = true }
    }
}

fun createCollectionMonitor(
    type: Class<out ViewItem>,
    pageSize: Long,
    orderBy: String = FirestoreConstants.TIMESTAMP
) = CollectionMonitor(createLiveData(type, pageSize), pageSize, orderBy)

private fun createLiveData(
    type: Class<out ViewItem>,
    pageSize: Long
): (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData =
    { query, setLastVisibleItem, onLastReached ->

        ItemsLiveData(query, setLastVisibleItem, onLastReached, type, pageSize)
    }
