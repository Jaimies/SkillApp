package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.daysAgo
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit.WEEKS

fun getAvgWeekHours(time: Int, startedAt: OffsetDateTime): String {
    val weekCount = WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = time / if (weekCount > 0) weekCount.toInt() else 1

    return getFriendlyHours(avgMins)
}

fun OffsetDateTime.getDaysSpentSince() = daysAgo.coerceAtLeast(0) + 1
