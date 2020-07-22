package com.jdevs.timeo.util

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

class ListDataSource<T>(list: List<T>) : PageKeyedDataSource<Int, T>() {

    private val provider = ListProvider(list)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {

        val list = provider.getList(0, params.requestedLoadSize)
        callback.onResult(list, 1, 2)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

        val list = provider.getList(params.key, params.requestedLoadSize)
        callback.onResult(list, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

        val list = provider.getList(params.key, params.requestedLoadSize)
        val nextIndex = if (params.key > 1) params.key - 1 else null
        callback.onResult(list, nextIndex)
    }

    class Factory<T>(private val list: List<T>) : DataSource.Factory<Int, T>() {

        override fun create() = ListDataSource(list)
    }

    private class ListProvider<T>(private val list: List<T>) {

        fun getList(page: Int, pageSize: Int): List<T> {

            val initialIndex = page * pageSize
            val finalIndex = initialIndex + pageSize

            return when {
                list.lastIndex < initialIndex -> emptyList()
                list.lastIndex < finalIndex -> list.subList(initialIndex, list.size)
                else -> list.subList(initialIndex, finalIndex)
            }
        }
    }
}
