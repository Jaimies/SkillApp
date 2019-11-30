package com.jdevs.timeo.data.operations

import com.jdevs.timeo.data.Record

data class RecordOperation(
    val item: Record?,
    override val type: Int,
    override val id: String = ""
) : Operation
