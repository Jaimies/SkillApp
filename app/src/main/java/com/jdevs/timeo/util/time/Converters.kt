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

fun getHours(totalMinutes: Long): String {

    val time = totalMinutes / HOUR_MINUTES.toFloat()
    val timeString = "%.1f".format(time)

    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun getHours(totalMinutes: Int) = getHours(totalMinutes.toLong())
fun getMins(hour: Int, minute: Int) = hour * HOUR_MINUTES + minute
