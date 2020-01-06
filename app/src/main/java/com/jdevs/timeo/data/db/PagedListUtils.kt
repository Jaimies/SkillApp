package com.jdevs.timeo.data.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jdevs.timeo.data.Mapper

fun <T : Mapper<Model>, Model> DataSource.Factory<Int, T>.toLivePagedList(
    pageSize: Int
): LiveData<PagedList<Model>> {

    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pageSize)
        .build()

    val pagedList = map { it.mapToDomain() }

    return LivePagedListBuilder(pagedList, pagedListConfig).build()
}
