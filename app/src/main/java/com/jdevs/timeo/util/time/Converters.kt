package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.HOUR_MINUTES

fun getFriendlyTime(totalMinutes: Int) = buildString {
    val hours = totalMinutes / HOUR_MINUTES
    val minutes = totalMinutes % HOUR_MINUTES

    if (hours != 0) append("${hours}h")
    if (minutes != 0) append(" ${minutes}m")
}.trim()

fun getFriendlyHours(totalMinutes: Int) = getHours(totalMinutes).toReadableFloat()

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}

fun getHours(minutes: Int) = minutes / HOUR_MINUTES.toFloat()
fun getMins(hours: Int, minutes: Int) = hours * HOUR_MINUTES + minutes
