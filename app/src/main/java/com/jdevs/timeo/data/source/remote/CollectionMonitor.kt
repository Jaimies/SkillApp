package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.LiveDataConstructor

class CollectionMonitor(
    private val pageSize: Long,
    private val liveData: LiveDataConstructor
) {

    private lateinit var query: Query
    private lateinit var ref: CollectionReference

    private var lastVisibleItem: DocumentSnapshot? = null
    private var isLastItemReached = false
    private val newQuery
        get() = ref
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(pageSize)

    fun setRef(ref: CollectionReference) {

        this.ref = ref
        reset()
    }

    fun reset() {

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
