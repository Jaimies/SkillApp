package com.jdevs.timeo.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <X, Y> map(source: LiveData<List<X>>, mapFunction: (X) -> Y): LiveData<List<Y>> {

    return Transformations.map(source) { it.map(mapFunction) }
}
