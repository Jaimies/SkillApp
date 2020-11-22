package com.jdevs.timeo.domain.model

import java.time.OffsetDateTime

data class Record(
    val name: String,
    val activityId: Id,
    val time: Duration,
    val id: Id = 0,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)
