package com.jdevs.timeo.domain.model

data class Operation(
    val data: DataItem? = null,
    val exception: Exception? = null,
    val type: Int
) {

    init {

        require(data == null || exception == null) { "Both data and exception can't be non-null" }
    }
}
