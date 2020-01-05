package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.common.adapter.ViewItem

fun createCollectionMonitor(
    type: Class<out ViewItem>,
    pageSize: Long
) = CollectionMonitor(createLiveData(type, pageSize), pageSize)

private fun createLiveData(
    type: Class<out ViewItem>,
    pageSize: Long
): (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData =
    { query, setLastVisibleItem, onLastReached ->

        ItemsLiveData(query, setLastVisibleItem, onLastReached, type, pageSize)
    }
