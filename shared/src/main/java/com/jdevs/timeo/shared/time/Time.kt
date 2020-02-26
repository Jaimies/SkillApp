package com.jdevs.timeo.shared.time

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit.DAYS
import org.threeten.bp.temporal.ChronoUnit.MONTHS
import org.threeten.bp.temporal.ChronoUnit.WEEKS
import java.util.Date

val currentOffset: ZoneOffset get() = OffsetDateTime.now().offset
val EPOCH: OffsetDateTime get() = Instant.EPOCH.atOffset(currentOffset)

fun Date?.toOffsetDate(): OffsetDateTime {

    if (this == null) {

        return OffsetDateTime.now()
    }

    return OffsetDateTime.from(DateTimeUtils.toInstant(this).atOffset(currentOffset))
}

fun OffsetDateTime.toDate(): Date = DateTimeUtils.toDate(toInstant())

fun OffsetDateTime.getDaysAgo() = DAYS.between(this, OffsetDateTime.now())
fun OffsetDateTime.getDaysSinceEpoch() = LocalDate.from(this).toEpochDay().toInt()
fun OffsetDateTime.getWeeksSinceEpoch() = WEEKS.between(EPOCH, this).toInt()
fun OffsetDateTime.getMonthSinceEpoch() = MONTHS.between(EPOCH, this).toInt()

const val WEEK_DAYS = 7
const val HOUR_MINUTES = 60
