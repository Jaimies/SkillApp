package com.jdevs.timeo

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    value = value
}

fun <T> MutableLiveData<MutableList<T>>.add(item: T) {

    value?.add(item)
    notifyObserver()
}

fun <T> MutableLiveData<MutableList<T>>.remove(item: T) {

    value?.remove(item)
    notifyObserver()
}

fun <T> MutableLiveData<MutableList<T>>.clear() {

    value?.clear()
    notifyObserver()
}

fun <T> MutableLiveData<out List<T>>.indexOfFirst(predicate: (T) -> Boolean): Int? {

    return value?.indexOfFirst(predicate)
}

operator fun <T> MutableLiveData<MutableList<T>>.set(index: Int, item: T) {

    value?.set(index, item)
    notifyObserver()
}
