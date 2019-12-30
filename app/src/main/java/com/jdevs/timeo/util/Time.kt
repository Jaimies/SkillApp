package com.jdevs.timeo.util

import com.jdevs.timeo.util.Time.HOUR_MINUTES
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit
import java.util.Date

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

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = ChronoUnit.DAYS.between(this, OffsetDateTime.now()) + 1
    return if (daysDiff > 0) daysDiff + 1 else 1
}

fun Date?.toOffsetDate(): OffsetDateTime {

    return if (this == null) {

        OffsetDateTime.now()
    } else {

        OffsetDateTime.from(DateTimeUtils.toInstant(this).atOffset(ZoneOffset.UTC))
    }
}

fun Long.getAvgDailyHours(timestamp: OffsetDateTime): String {

    val daysCount = timestamp.getDaysSpentSince()
    val avgDailyMins = this / daysCount

    return avgDailyMins.getHours()
}
