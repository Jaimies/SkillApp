package com.jdevs.timeo.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.livedata.ItemListLiveData

abstract class RemoteDataSource {

    var onLastItemCallback: () -> Unit = {}
        set(newCallback) {

            field = newCallback

            if (isLastItemReached) {

                newCallback()
            }
        }

    protected abstract val liveData: (Query?, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemListLiveData

    private lateinit var query: Query
    private var awaitingLiveData: ItemListLiveData? = null
    private var isLastItemReached = false
    private var isQueryInitialized = false
    private var lastVisibleItem: DocumentSnapshot? = null

    protected abstract fun createQuery(): Query
    abstract fun setRef(ref: CollectionReference)

    fun reset(ref: CollectionReference) {

        lastVisibleItem = null
        isLastItemReached = false
        awaitingLiveData = null
        isQueryInitialized = false

        setRef(ref)
    }

    fun getLiveData(): ItemListLiveData? {

        if (isLastItemReached) {

            return null
        }

        if (!isQueryInitialized) {

            query = createQuery()
            isQueryInitialized = true
        }

        val lastItem = lastVisibleItem

        if (lastItem != null) {

            query = query.startAfter(lastItem)
        }

        return liveData(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    fun getAwaitingLiveData(): ItemListLiveData {

        awaitingLiveData = liveData(null, ::setLastVisibleItem, ::onLastItemReached)
        return awaitingLiveData!!
    }

    fun onUserAuthenticated(ref: CollectionReference) {

        setRef(ref)
        query = createQuery()
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
