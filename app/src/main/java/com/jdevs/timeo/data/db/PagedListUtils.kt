package com.jdevs.timeo.data.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

fun <T> DataSource.Factory<Int, T>.toLivePagedList(
    pageSize: Int
): LiveData<PagedList<T>> {

    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()

    return LivePagedListBuilder(this, pagedListConfig).build()
}
