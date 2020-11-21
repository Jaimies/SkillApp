package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val name: String,
    val activityId: Id,
    val time: Int,
    val id: Id = 0,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)
