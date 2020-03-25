package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.daysAgo
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit

fun getAvgWeekHours(time: Int, startedAt: OffsetDateTime): String {

    val weekCount = ChronoUnit.WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = time / if (weekCount > 0) weekCount.toInt() else 1

    return getFriendlyHours(avgMins)
}

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = daysAgo
    return if (daysDiff > 0) daysDiff + 1 else 1
}
