package com.maxpoliakov.skillapp.util.time

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.Duration
import java.time.LocalDate

fun Duration.toReadableTime(context: Context) = buildString {
    val hours = toHours()
    val minutes = toMinutes() - hours * 60

    if (hours != 0L) append(context.getHoursString(hours))
    if (minutes != 0L) append(context.getMinutesString(minutes))
}.trim()

private fun Context.getHoursString(hours: Long) : String {
    return getString(R.string.hours, hours.toString())
}

private fun Context.getMinutesString(minutes: Long): String {
    return " " + getString(R.string.minutes, minutes.toString())
}

fun Duration.toReadableHours() = getHours(toMinutes()).toReadableFloat()

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}

fun Context.toReadableDate(date: LocalDate): String {
    if (date == getCurrentDate()) return getString(R.string.today)
    if (date == getCurrentDate().minusDays(1)) return getString(R.string.yesterday)
    if(date.year == getCurrentDate().year) return getString(R.string.date, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth)
    return getString(R.string.date_with_year, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth, date.year)
}

private const val HOUR_MINUTES = 60
private fun getHours(minutes: Long) = minutes / HOUR_MINUTES.toFloat()
