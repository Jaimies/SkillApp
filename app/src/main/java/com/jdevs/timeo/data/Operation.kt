package com.jdevs.timeo.data

import com.jdevs.timeo.common.adapter.ViewItem

data class Operation(
    val data: ViewItem? = null,
    val exception: Exception? = null,
    val type: Int
) {

    init {

        if (data != null && exception != null) {

            throw IllegalArgumentException("Both data and exception can't be non-null")
        }
    }
}
