package com.maxpoliakov.skillapp.shared.util

import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

inline fun <X, Y> Flow<List<X>>.mapList(crossinline transform: suspend (X) -> Y): Flow<List<Y>> {
    return this.map { list ->
        list.map { item -> transform(item) }
    }
}

suspend inline fun <T> Flow<T>.collectOnce(crossinline collector: (T) -> Unit) {
    coroutineScope {
        launch {
            collect { value ->
                collector.invoke(value)
                this.cancel()
            }
        }
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
