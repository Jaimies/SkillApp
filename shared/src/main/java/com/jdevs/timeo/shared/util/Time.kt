package com.jdevs.timeo.shared.util

import androidx.annotation.VisibleForTesting
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.WEEKS
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.Temporal
import java.util.Locale

val EPOCH: LocalDate get() = LocalDate.ofEpochDay(0)

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Temporal.daysAgo get() = DAYS.between(this, getCurrentDateTime())
val Temporal.daysSinceEpoch get() = DAYS.between(EPOCH, this)
val Temporal.weeksSinceEpoch get() = WEEKS.between(EPOCH, this)
val Temporal.monthSinceEpoch get() = MONTHS.between(EPOCH, this)

fun getCurrentDate() : LocalDate = LocalDate.now(clock)
fun getCurrentDateTime(): OffsetDateTime = OffsetDateTime.now(clock)

private var clock: Clock = Clock.systemDefaultZone()

@VisibleForTesting
fun setClock(newClock: Clock) {
    clock = newClock
}
