package com.jdevs.timeo.data

data class Operation(
    val activity: TimeoActivity?,
    val type: Int,
    val id : String = ""
)