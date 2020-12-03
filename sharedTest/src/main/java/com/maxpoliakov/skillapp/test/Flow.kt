package com.maxpoliakov.skillapp.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Flow<T>.await(): T {
    return suspendCoroutine { continuation ->
        CoroutineScope(Dispatchers.IO).launch {
            this@await.collect { value ->
                continuation.resume(value)
            }
        }
    }
}
