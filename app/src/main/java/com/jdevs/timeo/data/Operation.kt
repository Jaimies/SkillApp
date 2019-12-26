package com.jdevs.timeo.data

data class Operation(
    val data: Any? = null,
    val exception: Exception? = null,
    val type: Int
) {

    init {

        if (data != null && exception != null) {

            throw IllegalArgumentException("Both data and exception can't be non-null")
        }
    }
}
