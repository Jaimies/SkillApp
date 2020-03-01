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

fun getFriendlyHours(totalMinutes: Int): String {

    val time = getHours(totalMinutes)
    val timeString = "%.1f".format(time)

    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun getHours(minutes: Int) = minutes / HOUR_MINUTES.toFloat()
fun getMins(hour: Int, minute: Int) = hour * HOUR_MINUTES + minute
