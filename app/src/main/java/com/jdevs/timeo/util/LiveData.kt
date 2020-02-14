package com.jdevs.timeo.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.model.OperationItem
import com.jdevs.timeo.model.ViewItem

inline fun <T : Any, O : ViewItem> List<LiveData<Operation<T>>>.mapTo(
    crossinline mapFunction: (T) -> O
): List<LiveData<OperationItem<O>>> {

    return map { liveData ->

        map(liveData) { operation ->
            OperationItem(
                operation.data?.let { mapFunction(it) }, operation.exception, operation.type
            )
        }
    }
}
