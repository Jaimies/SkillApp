package com.jdevs.timeo.util

import com.jdevs.timeo.util.Time.HOUR_MINUTES
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

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

fun Date.getDaysSpentSince(): Int {

    val millisDiff = Calendar.getInstance().timeInMillis - time
    val daysDiff = TimeUnit.MILLISECONDS.toDays(millisDiff).toInt()

    return if (daysDiff > 0) daysDiff + 1 else 1
}

fun Long.getAvgDailyHours(timestamp: Date): String {

    val daysCount = timestamp.getDaysSpentSince()
    val avgDailyMins = this / daysCount

    return avgDailyMins.getHours()
}
