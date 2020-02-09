package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.getDaysAgo
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit

fun getProgress(minutes: Long) =
    minutes.toInt().rem(TWENTY_FIVE_HOURS) * PERCENT_COUNT / TWENTY_FIVE_HOURS

fun getPrevMilestone(time: Long) = time.toInt() - time.toInt().rem(TWENTY_FIVE_HOURS)
fun getNextMilestone(minutes: Long) = getPrevMilestone(minutes) + TWENTY_FIVE_HOURS

fun getAvgWeekHours(time: Long, startedAt: OffsetDateTime): String {

    val weekCount = ChronoUnit.WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = time / if (weekCount > 0) weekCount else 1

    return getHours(avgMins)
}

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = getDaysAgo()
    return if (daysDiff > 0) daysDiff + 1 else 1
}

private const val TWENTY_FIVE_HOURS = 25 * 60
private const val PERCENT_COUNT = 100
