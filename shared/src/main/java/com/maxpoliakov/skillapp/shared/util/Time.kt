package com.maxpoliakov.skillapp.shared.util

import androidx.annotation.VisibleForTesting
import java.time.Clock
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.Temporal
import java.util.Locale

val EPOCH: LocalDate get() = LocalDate.ofEpochDay(0)

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val LocalDate.daysAgo get() = DAYS.between(this, getCurrentDate())
val LocalDate.daysSinceEpoch get() = DAYS.between(EPOCH, this)

val LocalDate.millisSinceEpoch
    get() = Duration.ofDays(this.toEpochDay()).toMillis()

fun dateOfEpochMillis(millis: Long): LocalDate {
    val epochDay = Duration.ofMillis(millis).toDays()
    return LocalDate.ofEpochDay(epochDay)
}

fun Temporal.until(other: Temporal): Duration {
    val millis = this.until(other, MILLIS)
    return Duration.ofMillis(millis)
}

fun durationOfHoursAndMinutes(hours: Int, minutes: Int): Duration {
    val hours = Duration.ofHours(hours.toLong())
    val minutes = Duration.ofMinutes(minutes.toLong())
    return hours.plus(minutes)
}

fun String.toZonedDateTimeOrNull(): ZonedDateTime? {
    return try {
        ZonedDateTime.parse(this)
    } catch (e: DateTimeException) {
        null
    }
}

fun getCurrentDate(): LocalDate = LocalDate.now(clock)
fun getZonedDateTime(): ZonedDateTime = ZonedDateTime.now(clock)

private var clock: Clock = Clock.systemDefaultZone()

@VisibleForTesting
fun setClock(newClock: Clock) {
    clock = newClock
}
