package com.jdevs.timeo.util.time

import java.time.Duration

fun getFriendlyTime(duration: Duration) = buildString {
    val hours = duration.toHours()
    val minutes = duration.toMinutes() - hours * 60

    if (hours != 0L) append("${hours}h")
    if (minutes != 0L) append(" ${minutes}m")
}.trim()

fun getFriendlyHours(duration: Duration) = getHours(duration.toMinutes()).toReadableFloat()

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}

private const val HOUR_MINUTES = 60
private fun getHours(minutes: Long) = minutes / HOUR_MINUTES.toFloat()
