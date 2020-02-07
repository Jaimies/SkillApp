package com.jdevs.timeo.data.util

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit

fun getFriendlyTime(totalMinutes: Long): String {

    val hours = totalMinutes / HOUR_MINUTES
    val minutes = totalMinutes % HOUR_MINUTES

    val builder = StringBuilder()

    if (hours != 0L) builder.append("${hours}h")
    if (minutes != 0L) builder.append(" ${minutes}m")

    return builder.trim().toString()
}

fun getHours(totalMinutes: Long): String {

    val time = totalMinutes / HOUR_MINUTES.toFloat()
    val timeString = "%.1f".format(time)

    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun getHours(totalMinutes: Int) =
    getHours(totalMinutes.toLong())

fun getMins(hours: Long, minutes: Long) = hours * HOUR_MINUTES + minutes
fun getProgress(minutes: Long) =
    minutes.toInt().rem(TWENTY_FIVE_HOURS) * PERCENT_COUNT / TWENTY_FIVE_HOURS

fun getPrevMilestone(time: Long) =
    time.toInt() - time.toInt().rem(TWENTY_FIVE_HOURS) * PERCENT_COUNT / TWENTY_FIVE_HOURS

fun getNextMilestone(minutes: Long) = getPrevMilestone(minutes) + TWENTY_FIVE_HOURS

fun getAvgWeekHours(time: Long, startedAt: OffsetDateTime): String {

    val weekCount = ChronoUnit.WEEKS.between(startedAt, OffsetDateTime.now()) + 1
    val avgMins = time / if (weekCount > 0) weekCount else 1

    return getHours(avgMins)
}

const val HOUR_MINUTES = 60
const val DAY_HOURS = 24
const val WEEK_DAYS = 7
private const val TWENTY_FIVE_HOURS = 25 * 60
private const val PERCENT_COUNT = 100
