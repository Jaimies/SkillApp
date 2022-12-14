package com.maxpoliakov.skillapp.shared.util

import androidx.annotation.VisibleForTesting
import java.time.Clock
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.ChronoUnit.WEEKS
import java.time.temporal.Temporal
import java.time.temporal.WeekFields
import java.util.Locale

val EPOCH: LocalDate get() = LocalDate.ofEpochDay(0)

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val LocalDate.daysAgo get() = DAYS.between(this, getCurrentDate())
val LocalDate.daysSinceEpoch get() = DAYS.between(EPOCH, this)
val LocalDate.weeksSinceEpoch get() = WEEKS.between(EPOCH.atStartOfWeek(), this.atStartOfWeek())
val LocalDate.monthsSinceEpoch get() = MONTHS.between(EPOCH, this)

val FullLocalDateRange: ClosedRange<LocalDate>
    get() = LocalDate.ofYearDay(Int.MIN_VALUE, 1)..LocalDate.ofYearDay(Int.MAX_VALUE, 1)

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy  HH:mm:ss")

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
    val millis = this.until(other, MILLIS)
    return Duration.ofMillis(millis)
}

fun <T> ClosedRange<T>.toDuration(): Duration where T : Comparable<T>, T : Temporal {
    return start.until(endInclusive)
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
