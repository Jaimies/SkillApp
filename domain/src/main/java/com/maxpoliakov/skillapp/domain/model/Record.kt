package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.LocalDateTime

data class Record(
    val name: String,
    val activityId: Id,
    val time: Duration,
    val id: Id = 0,
    val timestamp: LocalDateTime = getCurrentDateTime()
)
