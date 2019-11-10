package com.jdevs.timeo.data

data class ActivityOperation(
    val activity: TimeoActivity?,
    val type: Int,
    val id: String = ""
)
