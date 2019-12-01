package com.jdevs.timeo.data.operations

import com.jdevs.timeo.data.TimeoActivity

data class ActivityOperation(
    override val item: TimeoActivity?,
    override val type: Int,
    override val id: String = ""
) : Operation
