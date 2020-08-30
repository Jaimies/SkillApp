package com.jdevs.timeo.shared.util

import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.Month
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.ChronoUnit.DAYS
import org.threeten.bp.temporal.ChronoUnit.MONTHS
import org.threeten.bp.temporal.ChronoUnit.WEEKS
import java.util.Locale

val currentOffset: ZoneOffset get() = OffsetDateTime.now().offset
val EPOCH: OffsetDateTime get() = Instant.EPOCH.atOffset(currentOffset)

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val OffsetDateTime.daysAgo get() = DAYS.between(this, OffsetDateTime.now())

fun getUnitsSinceEpoch(unit: ChronoUnit): Int {
    return OffsetDateTime.now().getUnitsSinceEpoch(unit)
}

fun OffsetDateTime.getUnitsSinceEpoch(unit: ChronoUnit): Int {
    return unit.between(EPOCH, this).toInt()
}

val OffsetDateTime.daysSinceEpoch get() = getUnitsSinceEpoch(DAYS)
val OffsetDateTime.weeksSinceEpoch get() = getUnitsSinceEpoch(WEEKS)
val OffsetDateTime.monthSinceEpoch get() = getUnitsSinceEpoch(MONTHS)

const val HOUR_MINUTES = 60
