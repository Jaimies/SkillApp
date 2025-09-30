package com.theskillapp.skillapp.shared.util

import androidx.annotation.VisibleForTesting
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.WeekFields
import java.util.Date
import java.util.Locale

val EPOCH: LocalDate get() = LocalDate.ofEpochDay(0)

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Month.fullLocalizedName: String
    get() {
        // using SimpleDateFormat instead of DateTimeFormatter, because
        // the DateTimeFormatter.ofPattern("LLLL").format() returns the month
        // name with the wrong declination, e.g. "червня" instead of "червень"
        val format = SimpleDateFormat("LLLL", Locale.getDefault())
        val date = EPOCH.withDayOfMonth(1).withMonth(this.value)
        return format.format(date.toDate())
    }

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy  HH:mm:ss")

fun LocalDate.toDate(): Date {
    val instant = this.atStartOfDay().toInstant(ZoneOffset.UTC)
    return Date.from(instant)
}

fun LocalDate.atStartOfWeek(): LocalDate {
    val date = with(WeekFields.of(Locale.getDefault()).firstDayOfWeek)
    if (date > this) return date.minusWeeks(1)
    return date
}

val LocalDate.millisSinceEpoch
    get() = Duration.ofDays(this.toEpochDay()).toMillis()

fun dateOfEpochMillis(millis: Long): LocalDate {
    val epochDay = Duration.ofMillis(millis).toDays()
    return LocalDate.ofEpochDay(epochDay)
}

fun Temporal.until(other: Temporal): Duration {
    val nanos = this.until(other, ChronoUnit.NANOS)
    return Duration.ofNanos(nanos)
}

fun ClosedRange<LocalTime>.toDuration(): Duration {
    val duration = start.until(endInclusive)
    if (duration.isNegative) return duration.plusDays(1)
    return duration
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

fun Duration.toMinutesPartCompat(): Long {
    return toMinutes() - toHours() * 60
}

fun Duration.toSecondsPartCompat(): Long {
    return seconds - toMinutes() * 60
}

private var clock: Clock = Clock.systemDefaultZone()

@VisibleForTesting
fun setClock(newClock: Clock) {
    clock = newClock
}
