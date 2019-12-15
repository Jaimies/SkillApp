package com.jdevs.timeo.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.livedata.ItemListLiveData
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.LiveDataConstructor

abstract class RemoteDataSource(
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

    private val newQuery
        get() = ref
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(fetchLimit)

    private lateinit var query: Query
    private lateinit var ref: CollectionReference
    private var awaitingLiveData: ItemListLiveData? = null
    private var isLastItemReached = false
    private var isQueryInitialized = false
    private var lastVisibleItem: DocumentSnapshot? = null

    fun reset(ref: CollectionReference) {

        lastVisibleItem = null
        isLastItemReached = false
        awaitingLiveData = null
        isQueryInitialized = false

        this.ref = ref
    }

    fun getLiveData(): ItemListLiveData? {

        if (isLastItemReached) {

            return null
        }

        if (!isQueryInitialized) {

            query = newQuery
            isQueryInitialized = true
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return livedata(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    fun getAwaitingLiveData(): ItemListLiveData {

        livedata(null, ::setLastVisibleItem, ::onLastItemReached).also {

            awaitingLiveData = it
            return it
        }
    }

    fun onUserAuthenticated(ref: CollectionReference) {

        this.ref = ref
        query = newQuery
        awaitingLiveData?.setQuery(query)
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
