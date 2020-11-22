package com.jdevs.timeo.domain.model

import com.jdevs.timeo.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.OffsetDateTime

data class Record(
    val name: String,
    val activityId: Id,
    val time: Duration,
    val id: Id = 0,
    val timestamp: OffsetDateTime = getCurrentDateTime()
)
