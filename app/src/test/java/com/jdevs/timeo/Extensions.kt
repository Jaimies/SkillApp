package com.jdevs.timeo

import androidx.lifecycle.MutableLiveData

/**
 * Extension functions that make manipulation over [MutableLiveData] easier
 */

fun <T> MutableLiveData<T>.notifyObserver() {
    value = value
}

fun <T> MutableLiveData<List<T>>.add(item: T) {

    if (value != null) {

        value = value?.plus(item)
        notifyObserver()
    }
}

fun <T> MutableLiveData<List<T>>.remove(item: T) {

    if (value != null) {

        value = value?.minus(item)
        notifyObserver()
    }
}

fun <T> MutableLiveData<List<T>>.clear() {

    if (value != null) {

        value = emptyList()
        notifyObserver()
    }
}

fun <T> MutableLiveData<out List<T>>.indexOfFirst(predicate: (T) -> Boolean): Int? {

    return value?.indexOfFirst(predicate)
}

operator fun <T> MutableLiveData<List<T>>.set(index: Int, item: T) {

    val newList = value?.toMutableList()?.also { it[index] = item }
    value = newList
    notifyObserver()
}
