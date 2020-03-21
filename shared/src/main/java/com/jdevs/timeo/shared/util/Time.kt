package com.jdevs.timeo.shared.util

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoUnit.DAYS
import org.threeten.bp.temporal.ChronoUnit.MONTHS
import org.threeten.bp.temporal.ChronoUnit.WEEKS
import java.util.Date
import java.util.Locale

val currentOffset: ZoneOffset get() = OffsetDateTime.now().offset
val EPOCH: OffsetDateTime get() = Instant.EPOCH.atOffset(currentOffset)

val Month.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

val DayOfWeek.shortName: String
    get() = getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun Date?.toOffsetDate(): OffsetDateTime {

    if (this == null) {

        return OffsetDateTime.now()
    }

    return OffsetDateTime.from(DateTimeUtils.toInstant(this).atOffset(currentOffset))
}

fun OffsetDateTime.toDate(): Date = DateTimeUtils.toDate(this.toInstant())

fun OffsetDateTime.getDaysAgo() = DAYS.between(this, OffsetDateTime.now())
fun OffsetDateTime.getDaysSinceEpoch() = LocalDate.from(this).toEpochDay().toInt()
fun OffsetDateTime.getWeeksSinceEpoch() = WEEKS.between(EPOCH, this).toInt()
fun OffsetDateTime.getMonthSinceEpoch() = MONTHS.between(EPOCH, this).toInt()

fun OffsetDateTime?.isDateAfter(other: OffsetDateTime) =
    this?.toLocalDate()?.isAfter(other.toLocalDate()) ?: false

const val WEEK_DAYS = 7
const val HOUR_MINUTES = 60