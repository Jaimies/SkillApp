package com.theskillapp.skillapp.test

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import kotlinx.coroutines.Dispatchers

private val differCallback = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

suspend fun <T : Any> PagingData<T>.awaitData(): List<T> {
    val items = mutableListOf<T>()

    val differ = object : PagingDataDiffer<T>(differCallback, Dispatchers.Default) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (index in 0 until newList.size)
                items.add(newList.getFromStorage(index))

            onListPresentable()

            return null
        }
    }

    differ.collectFrom(this)
    return items
}
