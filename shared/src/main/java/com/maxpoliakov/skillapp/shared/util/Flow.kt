package com.maxpoliakov.skillapp.shared.util

import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

suspend inline fun <T> Flow<T>.collectIgnoringInitialValue(crossinline action: suspend (value: T) -> Unit) {
    var firstValueReceived = false

    collect { value ->
        if (!firstValueReceived) {
            firstValueReceived = true
            return@collect
        }

        action(value)
    }
}
