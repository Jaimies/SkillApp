package com.jdevs.timeo.data.operations

import com.jdevs.timeo.data.TimeoActivity

data class ActivityOperation(
    val activity: TimeoActivity?,
    val type: Int,
    val id: String = ""
)
