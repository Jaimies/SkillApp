package com.jdevs.timeo.util

import org.joda.time.DateTime
import org.joda.time.Days
import java.util.Date

private const val HOUR_MINUTES = 60

fun Long.getFriendlyTime(): String {

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

fun Long.getHours(): String {

    val time = this / HOUR_MINUTES.toFloat()

    val timeString = "%.1f".format(time)

    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun Pair<Long, Long>.getMins(): Long {
    return first * HOUR_MINUTES + second
}

fun Date.getDaysSince(): Int {

    val currentTime = DateTime()

    val creationTime = DateTime(this)

    val daysDiff = Days.daysBetween(creationTime, currentTime)

    return if (daysDiff.days > 0) daysDiff.days + 1 else 1
}

fun Long.getAvgDailyHours(timestamp: Date): String {

    val daysCount = timestamp.getDaysSince()
    val avgDailyMins = this / (daysCount + 1)

    return avgDailyMins.getHours()
}
