package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.daysAgo
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit

fun getProgress(minutes: Int) = minutes % TWENTY_FIVE_HOURS * PERCENT_COUNT / TWENTY_FIVE_HOURS

fun getPrevMilestone(time: Int) = time - time % TWENTY_FIVE_HOURS
fun getNextMilestone(minutes: Int) = getPrevMilestone(minutes) + TWENTY_FIVE_HOURS

fun getAvgWeekHours(time: Int, startedAt: OffsetDateTime): String {

    val weekCount = ChronoUnit.WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = time / if (weekCount > 0) weekCount.toInt() else 1

    return getFriendlyHours(avgMins)
}

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = daysAgo
    return if (daysDiff > 0) daysDiff + 1 else 1
}

private const val TWENTY_FIVE_HOURS = 25 * 60
private const val PERCENT_COUNT = 100
