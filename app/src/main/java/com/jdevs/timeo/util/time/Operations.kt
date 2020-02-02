package com.jdevs.timeo.util.time

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

val currentOffset: ZoneOffset get() = OffsetDateTime.now().offset
private val EPOCH = Instant.EPOCH.atOffset(currentOffset)

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = getDaysAgo()
    return if (daysDiff > 0) daysDiff + 1 else 1
}

fun OffsetDateTime.getDaysAgo() = ChronoUnit.DAYS.between(this, OffsetDateTime.now())

fun OffsetDateTime.getDaysSinceEpoch() = LocalDate.from(this).toEpochDay()

fun OffsetDateTime.getWeeksSinceEpoch(): Int {

    val epoch = OffsetDateTime.from(EPOCH)
    return ChronoUnit.WEEKS.between(epoch, this).toInt()
}

fun OffsetDateTime.getMonthSinceEpoch(): Short {

    val epoch = OffsetDateTime.from(EPOCH)
    return ChronoUnit.MONTHS.between(epoch, this).toShort()
}

fun getFriendlyDate(day: Long) = LocalDate.ofEpochDay(day).toString()

fun getFriendlyMonth(month: Int): String {

    val date = LocalDate.from(EPOCH).plusMonths(month.toLong())
    return "${date.year} - ${date.month}"
}

fun getFriendlyWeek(week: Int): String {

    val date = LocalDate.from(EPOCH).plusWeeks(week.toLong())
    val weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

    return "${date.year} / Week ${date.get(weekField)}"
}
