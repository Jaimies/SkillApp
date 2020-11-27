package com.maxpoliakov.skilapp.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> LiveData<T>.awaitValue() : T {
    return withContext(Dispatchers.Main) {
        suspendCoroutine<T> { continuation ->
            observeForever(continuation::resume)
        }
    }
}
