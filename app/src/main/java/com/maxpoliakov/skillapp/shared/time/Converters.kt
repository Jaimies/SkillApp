package com.maxpoliakov.skillapp.shared.time

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.LocalDate

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}

fun Context.toReadableDate(date: LocalDate?, currentDate: LocalDate): String {
    if (date == null) return ""
    if (date == currentDate) return getString(R.string.today)
    if (date == currentDate.minusDays(1)) return getString(R.string.yesterday)
    if (date.year == currentDate.year) return getString(R.string.date, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth)
    return getString(R.string.date_with_year, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth, date.year)
}

fun Context.toShortReadableDate(date: LocalDate): String {
    return getString(R.string.date_month_and_day, date.month.shortName, date.dayOfMonth)
}
