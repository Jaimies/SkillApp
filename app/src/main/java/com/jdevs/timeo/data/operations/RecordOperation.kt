package com.jdevs.timeo.data.operations

import com.jdevs.timeo.data.Record

data class RecordOperation(
    override val item: Record?,
    override val type: Int,
    override val id: String = ""
) : Operation
