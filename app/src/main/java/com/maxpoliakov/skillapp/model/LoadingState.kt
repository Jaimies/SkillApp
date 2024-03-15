package com.maxpoliakov.skillapp.model

sealed class LoadingState<out T> {
    object Loading : LoadingState<Nothing>()
    object Error : LoadingState<Nothing>()

    data class Success<T>(val value: T) : LoadingState<T>()
}
