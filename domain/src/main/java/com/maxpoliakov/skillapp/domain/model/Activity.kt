package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.LocalDateTime

data class Activity(
    val name: String,
    val totalTime: Duration,
    val lastWeekTime: Duration = Duration.ZERO,
    val id: Id = 0,
    val timestamp: LocalDateTime = getCurrentDateTime()
)
