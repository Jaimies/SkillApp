package com.jdevs.timeo.data

import androidx.paging.PagedList
import com.jdevs.timeo.ItemDataSource
import kotlinx.coroutines.runBlocking

fun <T> List<T>.asPagedList(): PagedList<T> {

    return PagedList.Builder<Int, T>(
        ItemDataSource(this), pagedListConfig
    )
        .setFetchExecutor { runBlocking { it.run() } }
        .setNotifyExecutor { runBlocking { it.run() } }
        .build()
}

private const val PAGE_SIZE = 50

private val pagedListConfig = PagedList.Config.Builder()
    .setInitialLoadSizeHint(PAGE_SIZE)
    .setPageSize(PAGE_SIZE)
    .build()

