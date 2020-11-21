package com.jdevs.timeo.shared.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.ChronoUnit.WEEKS
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
