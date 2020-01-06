package com.jdevs.timeo.data.firestore

import com.jdevs.timeo.common.adapter.ViewItem

data class Operation(
    val data: ViewItem? = null,
    val exception: Exception? = null,
    val type: Int
) {

    init {

        require(data == null || exception == null) { "Both data and exception can't be non-null" }
    }
}
