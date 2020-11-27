package com.maxpoliakov.skilapp.data.stats

import androidx.paging.DataSource
import androidx.paging.toLiveData

suspend fun <T> DataSource.Factory<*, T>.getValue(): List<T> {
    return this.toLiveData(Int.MAX_VALUE).awaitValue()
}
