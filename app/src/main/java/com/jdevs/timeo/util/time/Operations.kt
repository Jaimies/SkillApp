package com.jdevs.timeo.util.time

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

private val currentOffset get() = OffsetDateTime.now().offset
private val EPOCH = Instant.EPOCH.atOffset(currentOffset)

fun OffsetDateTime.getDaysSpentSince(): Long {

    val daysDiff = getDaysAgo()
    return if (daysDiff > 0) daysDiff + 1 else 1
}

fun OffsetDateTime.getDaysAgo(): Long {

    return ChronoUnit.DAYS.between(this, OffsetDateTime.now())
}

fun OffsetDateTime.getDaysSinceEpoch(): Long {

    return LocalDate.from(this).toEpochDay()
}

fun OffsetDateTime.getWeeksSinceEpoch(): Int {

    val epoch = OffsetDateTime.from(EPOCH)
    return ChronoUnit.WEEKS.between(epoch, this).toInt()
}

fun OffsetDateTime.getMonthSinceEpoch(): Short {

    val epoch = OffsetDateTime.from(EPOCH)
    return ChronoUnit.MONTHS.between(epoch, this).toShort()
}

fun Long.toFriendlyDate(): String {

    return LocalDate.ofEpochDay(this).toString()
}

fun Short.toFriendlyMonth(): String {

    val date = LocalDate.from(EPOCH).plusMonths(toLong())

    return "${date.year} - ${date.month}"
}

fun Short.toFriendlyWeek(): String {

    val date = LocalDate.from(EPOCH).plusWeeks(toLong())

    val weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()

    return "${date.year} / Week ${date.get(weekField)}"
}
