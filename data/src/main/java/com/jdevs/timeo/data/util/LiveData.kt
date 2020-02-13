package com.jdevs.timeo.data.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

internal fun <X, Y> map(source: LiveData<List<X>>, mapFunction: (X) -> Y): LiveData<List<Y>> {

    return Transformations.map(source) { it.map(mapFunction) }
}
