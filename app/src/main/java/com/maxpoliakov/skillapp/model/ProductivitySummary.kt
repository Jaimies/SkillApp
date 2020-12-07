package com.maxpoliakov.skillapp.model

import java.time.Duration

data class ProductivitySummary(
    val totalTime: Duration,
    val averageWeekTime: Duration,
    val lastWeekTime: Duration
)
