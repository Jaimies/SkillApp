package com.jdevs.timeo.util.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Operation.Changed
import com.jdevs.timeo.model.ViewItem

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any, O : ViewItem> List<LiveData<Operation<T>>>.mapOperation(
    crossinline transform: (T) -> O
): List<LiveData<Operation<O>>> {
    return map { liveData ->
        liveData.map { operation ->
            if (operation is Changed) Changed(transform(operation.item), operation.changeType)
            else operation as Operation<O>
        }
    }
}
