package com.jdevs.timeo.util

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

fun <I, O> DataSource.Factory<Int, I>.toLiveData(
    pageSize: Int,
    mapper: (I) -> O
): LiveData<PagedList<O>> {

    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()

    val pagedList = map(mapper)

    return LivePagedListBuilder(pagedList, pagedListConfig).build()
}
