package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TOTAL_TIME

private typealias LiveDataConstructor<T> = (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ListLiveData<*, T>

class QueryWatcher<T : Any>(
    private val liveData: LiveDataConstructor<T>,
    private val pageSize: Long,
    private val orderBy: String
) {

    private lateinit var query: Query
    private var lastDocument: DocumentSnapshot? = null
    private var isLastItemReached = false
    private val liveDatas = mutableListOf<ListLiveData<*, T>>()

    fun setQuery(query: Query) {

        lastDocument = null
        isLastItemReached = false
        this.query = query.orderBy(orderBy, Query.Direction.DESCENDING).limit(pageSize)
        liveDatas.clear()
    }

    fun getLiveDataList(fetchNewItems: Boolean): List<ListLiveData<*, T>> {

        if (!isLastItemReached && (fetchNewItems || liveDatas.isEmpty())) {

            lastDocument?.let { query = query.startAfter(it) }
            val liveData = liveData(query, { lastDocument = it }) { isLastItemReached = true }
            liveDatas.add(liveData)
        }

        return liveDatas
    }

    companion object {

        inline operator fun <reified T : Any, O : Any> invoke(
            pageSize: Long,
            noinline mapFunction: (T) -> O,
            orderBy: String = TOTAL_TIME
        ) = QueryWatcher(createLiveDataConstructor(pageSize, mapFunction), pageSize, orderBy)

        inline fun <reified T : Any, O : Any> createLiveDataConstructor(
            pageSize: Long,
            noinline mapFunction: (T) -> O
        ): LiveDataConstructor<O> = { query, setLastDoc, onLastReached ->

            ListLiveData(query, setLastDoc, onLastReached, T::class.java, mapFunction, pageSize)
        }
    }
}
