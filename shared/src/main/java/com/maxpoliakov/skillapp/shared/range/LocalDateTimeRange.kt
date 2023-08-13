package com.maxpoliakov.skillapp.shared.range

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun ClosedRange<LocalDateTime>.split(dayStartTime: LocalTime): List<PartOfDateTimeRange> {
    val dayEndTime = getDayEndTime(dayStartTime)

    val (startDate, startTime) = start.toLocalDateAndTimeWithRespectToDayStartTime(dayStartTime)
    val (endDate, endTime) = endInclusive.toLocalDateAndTimeWithRespectToDayStartTime(dayStartTime)

    if (startDate == endDate) {
        return listOf(PartOfDateTimeRange(startDate, startTime..endTime))
    }

    val records = mutableListOf(
        PartOfDateTimeRange(startDate, startTime..dayEndTime),
        PartOfDateTimeRange(endDate, dayStartTime..endTime),
    )

    val numOfRecords = ChronoUnit.DAYS.between(startDate, endDate) - 1

    for (addedDays in 1..numOfRecords) {
        val date = startDate.plusDays(addedDays)
        records.add(records.lastIndex, PartOfDateTimeRange(date, dayStartTime..dayEndTime))
    }

    return records
}

private fun LocalDateTime.toLocalDateAndTimeWithRespectToDayStartTime(dayStartTime: LocalTime): Pair<LocalDate, LocalTime> {
    val time = toLocalTime()
    val date = if (time >= dayStartTime) toLocalDate() else toLocalDate().minusDays(1)
    return date to time
}

private fun getDayEndTime(dayStartTime: LocalTime): LocalTime {
    return dayStartTime.minusNanos(1)
}
