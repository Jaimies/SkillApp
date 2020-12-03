package com.maxpoliakov.skillapp.shared.util

import androidx.annotation.VisibleForTesting
import java.time.Clock
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.Temporal
import java.util.Locale

val EPOCH: LocalDate get() = LocalDate.ofEpochDay(0)

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Temporal.daysAgo get() = DAYS.between(this, getCurrentDate())
val Temporal.daysSinceEpoch get() = DAYS.between(EPOCH, this)

val LocalDate.millisSinceEpoch
    get() = Duration.ofDays(this.toEpochDay()).toMillis()

fun dateOfEpochMillis(millis: Long) : LocalDate {
    val epochDay = Duration.ofMillis(millis).toDays()
    return LocalDate.ofEpochDay(epochDay)
}

fun getCurrentDate(): LocalDate = LocalDate.now(clock)

private var clock: Clock = Clock.systemDefaultZone()

@VisibleForTesting
fun setClock(newClock: Clock) {
    clock = newClock
}
