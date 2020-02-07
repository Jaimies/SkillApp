package com.jdevs.timeo.ui.model

data class OperationItem<T : ViewItem>(
    val data: T? = null,
    val exception: Exception? = null,
    val type: Int
) {

    init {

        require(data == null || exception == null) { "Both data and exception can't be non-null" }
    }
}
