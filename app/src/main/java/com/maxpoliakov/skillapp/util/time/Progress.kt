package com.maxpoliakov.skillapp.util.time

import com.maxpoliakov.skillapp.shared.util.daysAgo
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.WEEKS

fun getAvgWeekHours(duration: Duration, startedAt: LocalDateTime): String {
    val weekCount = WEEKS.between(startedAt, getCurrentDateTime()) + 1
    val avgMins = duration.dividedBy(weekCount.coerceAtLeast(1L))

    return getFriendlyHours(avgMins)
}

fun LocalDateTime.getDaysSpentSince() = daysAgo.coerceAtLeast(0) + 1
