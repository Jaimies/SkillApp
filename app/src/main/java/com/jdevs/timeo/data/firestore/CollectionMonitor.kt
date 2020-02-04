package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.ui.common.adapter.ViewItem
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
import kotlin.reflect.KClass

typealias  LiveDataConstructor = (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData

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

    private val liveDatas = mutableListOf<ItemsLiveData>()

    fun setRef(ref: CollectionReference) {

        this.ref = ref
        lastVisibleItem = null
        isLastItemReached = false
        query = newQuery
        liveDatas.clear()
    }

    fun getLiveDataList(): List<ItemsLiveData> {

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

fun <T : Any> createCollectionMonitor(
    type: KClass<out T>,
    mapper: Mapper<T, ViewItem>,
    pageSize: Long,
    orderBy: String = TOTAL_TIME
) = CollectionMonitor(createLiveData(type, pageSize, mapper), pageSize, orderBy)

@Suppress("UNCHECKED_CAST")
private fun <T : Any> createLiveData(
    type: KClass<out T>,
    pageSize: Long,
    mapper: Mapper<T, ViewItem>
): LiveDataConstructor =
    { query, setLastVisibleItem, onLastReached ->

        ItemsLiveData(
            query, setLastVisibleItem, onLastReached,
            type.java, mapper as Mapper<Any, ViewItem>,
            pageSize
        )
    }
