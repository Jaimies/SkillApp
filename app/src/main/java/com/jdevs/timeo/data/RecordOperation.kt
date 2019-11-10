package com.jdevs.timeo.data

data class RecordOperation(
    val activity: TimeoRecord?,
    val type: Int,
    val id: String = ""
)
