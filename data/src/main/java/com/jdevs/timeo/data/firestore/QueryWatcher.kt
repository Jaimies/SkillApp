package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.jdevs.timeo.data.TOTAL_TIME

class QueryWatcher<T : Any, O : Any>(
    private val pageSize: Long,
    private val orderBy: String,
    private val modelClass: Class<T>,
    private val mapFunction: (T) -> O
) {
    private lateinit var query: Query
    private var lastDocument: DocumentSnapshot? = null
    private var isLastItemReached = false
    private val liveDatas = mutableListOf<ListLiveData<*, O>>()

    fun setQuery(query: Query) {
        lastDocument = null
        isLastItemReached = false
        this.query = query.orderBy(orderBy, DESCENDING).limit(pageSize)
        liveDatas.clear()
    }

    fun getLiveDataList(fetchNewItems: Boolean): List<ListLiveData<*, O>> {

        if (!isLastItemReached && (fetchNewItems || liveDatas.isEmpty())) {

            lastDocument?.let { query = query.startAfter(it) }

            val liveData = ListLiveData(
                query, { lastDocument = it }, { isLastItemReached = true },
                modelClass, mapFunction, pageSize
            )

            liveDatas.add(liveData)
        }

        return liveDatas
    }

    companion object {
        inline operator fun <reified T : Any, O : Any> invoke(
            pageSize: Long,
            noinline mapFunction: (T) -> O,
            orderBy: String = TOTAL_TIME
        ) = QueryWatcher(pageSize, orderBy, T::class.java, mapFunction)
    }
}
