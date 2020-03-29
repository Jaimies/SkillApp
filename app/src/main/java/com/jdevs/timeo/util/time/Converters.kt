package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.util.HOUR_MINUTES

fun getFriendlyTime(totalMinutes: Int): String {

    val hours = totalMinutes / HOUR_MINUTES
    val minutes = totalMinutes % HOUR_MINUTES

    val builder = StringBuilder()

    if (hours != 0) builder.append("${hours}h")
    if (minutes != 0) builder.append(" ${minutes}m")

    return builder.trim().toString()
}

fun getFriendlyHours(totalMinutes: Int) = getHours(totalMinutes).toReadableFloat()

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}

fun getHours(minutes: Int) = minutes / HOUR_MINUTES.toFloat()
fun getMins(hour: Int, minute: Int) = hour * HOUR_MINUTES + minute
