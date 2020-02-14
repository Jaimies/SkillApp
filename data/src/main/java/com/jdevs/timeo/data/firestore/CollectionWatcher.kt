package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TOTAL_TIME

private typealias LiveDataConstructor<T> = (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ListLiveData<*, T>

class CollectionWatcher<T : Any>(
    private val liveData: LiveDataConstructor<T>,
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

    private val liveDatas = mutableListOf<ListLiveData<*, T>>()

    fun setRef(ref: CollectionReference) {

        this.ref = ref
        lastVisibleItem = null
        isLastItemReached = false
        query = newQuery
        liveDatas.clear()
    }

    fun getLiveDataList(): List<ListLiveData<*, T>> {

        val lastItem = lastVisibleItem

        if (!isLastItemReached) {

            if (lastItem != null) {

                query = query.startAfter(lastItem)
            }

            val liveData = liveData(query, { lastVisibleItem = it }) { isLastItemReached = true }
            liveDatas.add(liveData)
        }

        return liveDatas
    }
}

inline fun <reified T : Any, O : Any> createCollectionWatcher(
    pageSize: Long,
    noinline mapFunction: (T) -> O,
    orderBy: String = TOTAL_TIME
) = CollectionWatcher(createLiveData(T::class.java, pageSize, mapFunction), pageSize, orderBy)

fun <T : Any, O : Any> createLiveData(
    type: Class<T>,
    pageSize: Long,
    mapFunction: (T) -> O
): LiveDataConstructor<O> = { query, setLastVisibleItem, onLastReached ->

    ListLiveData(query, setLastVisibleItem, onLastReached, type, mapFunction, pageSize)
}
