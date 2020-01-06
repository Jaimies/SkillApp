package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY

fun createCollectionMonitor(
    type: Class<out ViewItem>,
    pageSize: Long,
    orderBy: String = TIMESTAMP_PROPERTY
) = CollectionMonitor(createLiveData(type, pageSize), pageSize, orderBy)

private fun createLiveData(
    type: Class<out ViewItem>,
    pageSize: Long
): (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData =
    { query, setLastVisibleItem, onLastReached ->

        ItemsLiveData(query, setLastVisibleItem, onLastReached, type, pageSize)
    }
