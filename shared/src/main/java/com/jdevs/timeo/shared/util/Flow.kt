package com.jdevs.timeo.shared.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <X, Y> Flow<List<X>>.mapList(crossinline transform: suspend (X) -> Y): Flow<List<Y>> {
    return this.map { list ->
        list.map { item -> transform(item) }
    }
}
