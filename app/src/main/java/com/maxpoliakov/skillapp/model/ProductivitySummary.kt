package com.maxpoliakov.skillapp.model

import java.time.Duration

data class ProductivitySummary(
    val totalTime: Duration,
    val timeToday: Duration,
    val lastWeekTime: Duration
) {
    companion object {
        val ZERO = ProductivitySummary(Duration.ZERO, Duration.ZERO, Duration.ZERO)
    }
}
