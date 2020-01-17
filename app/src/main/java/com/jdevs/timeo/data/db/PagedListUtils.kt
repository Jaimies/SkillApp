package com.jdevs.timeo.data.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jdevs.timeo.data.Mapper

fun <I, O> DataSource.Factory<Int, I>.toLivePagedList(
    pageSize: Int, mapper: Mapper<I, O>
): LiveData<PagedList<O>> {

    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()

    val pagedList = map(mapper::map)

    return LivePagedListBuilder(pagedList, pagedListConfig).build()
}
