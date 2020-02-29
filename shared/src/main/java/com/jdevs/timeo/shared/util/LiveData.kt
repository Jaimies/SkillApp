package com.jdevs.timeo.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.map

inline fun <X, Y> LiveData<List<X>>.mapList(crossinline transform: (X) -> Y): LiveData<List<Y>> {

    return map { list -> list.map(transform) }
}
