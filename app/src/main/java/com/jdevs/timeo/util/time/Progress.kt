package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.daysAgo
import java.time.Duration
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit.WEEKS

fun getAvgWeekHours(duration: Duration, startedAt: OffsetDateTime): String {
    val weekCount = WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = duration.dividedBy(weekCount.coerceAtLeast(1L))

    return getFriendlyHours(avgMins)
}

fun OffsetDateTime.getDaysSpentSince() = daysAgo.coerceAtLeast(0) + 1
