package com.jdevs.timeo.data.operations

import com.jdevs.timeo.data.Record

data class RecordOperation(
    val item: Record?,
    val type: Int,
    val id: String = ""
)
