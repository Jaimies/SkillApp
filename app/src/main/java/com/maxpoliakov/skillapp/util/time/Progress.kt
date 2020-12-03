package com.maxpoliakov.skillapp.util.time

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit.WEEKS

fun getAvgWeekHours(duration: Duration, startedAt: LocalDate): String {
    val weekCount = WEEKS.between(startedAt, getCurrentDate()) + 1
    val avgMins = duration.dividedBy(weekCount.coerceAtLeast(1L))

    return getFriendlyHours(avgMins)
}
