package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.EPOCH
import com.jdevs.timeo.shared.time.HOUR_MINUTES
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

fun getFriendlyDate(day: Long) = LocalDate.ofEpochDay(day).toString()

fun getFriendlyMonth(month: Int): String {

    val date = EPOCH.plusMonths(month.toLong())
    return "${date.year} - ${date.month}"
}

fun getFriendlyWeek(week: Int): String {

    val date = EPOCH.plusWeeks(week.toLong())
    val weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

    return "${date.year} / Week ${date.get(weekField)}"
}

fun getFriendlyTime(totalMinutes: Long): String {

    val hours = totalMinutes / HOUR_MINUTES
    val minutes = totalMinutes % HOUR_MINUTES

    val builder = StringBuilder()

    if (hours != 0L) builder.append("${hours}h")
    if (minutes != 0L) builder.append(" ${minutes}m")

    return builder.trim().toString()
}

fun getHours(totalMinutes: Long): String {

    val time = totalMinutes / HOUR_MINUTES.toFloat()
    val timeString = "%.1f".format(time)

    return if (timeString.takeLast(1) == "0") timeString.dropLast(2) else timeString
}

fun getHours(totalMinutes: Int) = getHours(totalMinutes.toLong())
