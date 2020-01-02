package com.jdevs.timeo.util.time

import com.jdevs.timeo.util.Time.HOUR_MINUTES
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

fun Pair<Long, Long>.toMins(): Long {

    return first * HOUR_MINUTES + second
}

fun Long.getAvgDailyHours(timestamp: OffsetDateTime): String {

    val daysCount = timestamp.getDaysSpentSince()
    val avgDailyMins = this / daysCount

    return avgDailyMins.toHours()
}
