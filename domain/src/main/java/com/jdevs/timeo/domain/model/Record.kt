package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: Id,
    val name: String,
    val activityId: Id,
    val time: Int,
    val timestamp: OffsetDateTime
)
