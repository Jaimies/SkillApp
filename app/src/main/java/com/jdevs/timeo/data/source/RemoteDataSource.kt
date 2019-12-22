package com.jdevs.timeo.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.ItemsLiveData
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.LiveDataConstructor

class RemoteDataSource(
    private val fetchLimit: Long,
    private val livedata: LiveDataConstructor
) {

    var onLastItemCallback: () -> Unit = {}
        set(newCallback) {

            field = newCallback

            if (isLastItemReached) {

                newCallback()
            }
        }

    private lateinit var query: Query
    private lateinit var ref: CollectionReference

    private var lastVisibleItem: DocumentSnapshot? = null
    private var isLastItemReached = false
    private var shouldInitializeQuery = true

    fun setup(ref: CollectionReference) {

        lastVisibleItem = null
        isLastItemReached = false
        shouldInitializeQuery = true

        this.ref = ref
    }

    fun getLiveData(): ItemsLiveData? {

        if (isLastItemReached) {

            return null
        }

        if (shouldInitializeQuery) {

            query = ref
                .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
                .limit(fetchLimit)
            shouldInitializeQuery = false
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return livedata(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    private fun onLastItemReached() {

        if (!isLastItemReached) {

            isLastItemReached = true
            onLastItemCallback()
        }
    }

    private fun setLastVisibleItem(lastVisibleItem: DocumentSnapshot) {

        this.lastVisibleItem = lastVisibleItem
    }
}
