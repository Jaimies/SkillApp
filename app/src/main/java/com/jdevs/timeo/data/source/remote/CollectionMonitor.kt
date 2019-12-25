package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.LiveDataConstructor

class CollectionMonitor(
    private val fetchLimit: Long,
    private val liveData: LiveDataConstructor
) {

    private lateinit var query: Query
    private lateinit var ref: CollectionReference

    private var lastVisibleItem: DocumentSnapshot? = null
    private var isLastItemReached = false

    fun setRef(ref: CollectionReference) {

        this.ref = ref
    }

    fun getLiveData(): ItemsLiveData? {

        if (isLastItemReached) {

            return null
        }

        if (!::query.isInitialized) {

            query = ref
                .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
                .limit(fetchLimit)
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return liveData(query, { lastVisibleItem = it }) { isLastItemReached = true }
    }
}
