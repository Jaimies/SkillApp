package com.theskillapp.skillapp.shared.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <X, Y> Flow<List<X>>.mapList(crossinline transform: suspend (X) -> Y): Flow<List<Y>> {
    return this.map { list ->
        list.map { item -> transform(item) }
    }
}

inline fun <T> Flow<List<T>>.filterList(crossinline transform: suspend (T) -> Boolean): Flow<List<T>> {
    return this.map { list ->
        list.filter { item -> transform(item) }
    }
}
