package com.jdevs.timeo.data

data class OperationOrException(
    val item: Any? = null,
    val exception: Exception? = null,
    val type: Int,
    val id: String = ""
)
