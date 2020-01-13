package com.jdevs.timeo.util.time

import org.threeten.bp.OffsetDateTime

fun Long.toFriendlyTime(): String {

    val hours = this / HOUR_MINUTES
    val minutes = this % HOUR_MINUTES

    var timeString = ""

    if (hours != 0L) {

        timeString += "${hours}h"
    }

    if (minutes != 0L) {

        if (hours != 0L) {

            timeString += " "
        }

        timeString += "${minutes}m"
    }

    return timeString
}

fun Long.toHours(): String {

    val time = this / HOUR_MINUTES.toFloat()

    val timeString = "%.1f".format(time)
    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun Long.getProgress() = toInt().rem(TWENTY_FIVE_HOURS) * PERCENT_COUNT / TWENTY_FIVE_HOURS
fun Long.getPrevMilestone() = toInt() - rem(TWENTY_FIVE_HOURS)
fun Long.getNextMilestone() = getPrevMilestone() + TWENTY_FIVE_HOURS

fun Int.toHours() = toLong().toHours()

fun Pair<Long, Long>.toMins() = first * HOUR_MINUTES + second

fun Long.getAvgDailyHours(timestamp: OffsetDateTime): String {

    val daysCount = timestamp.getDaysSpentSince()
    val avgDailyMins = this / daysCount

    return avgDailyMins.toHours()
}

const val HOUR_MINUTES = 60
const val DAY_HOURS = 24
const val WEEK_DAYS = 7
private const val TWENTY_FIVE_HOURS = 25 * 60
private const val PERCENT_COUNT = 100
