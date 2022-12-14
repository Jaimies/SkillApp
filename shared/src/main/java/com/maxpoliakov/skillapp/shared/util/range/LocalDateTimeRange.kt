package com.maxpoliakov.skillapp.shared.util.range

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun ClosedRange<LocalDateTime>.split(): List<PartOfDateTimeRange> {
    val startDate = start.toLocalDate()
    val startTime = start.toLocalTime()
    val endDate = endInclusive.toLocalDate()
    val endTime = endInclusive.toLocalTime()

    if (startDate == endDate) {
        return listOf(PartOfDateTimeRange(startDate, startTime..endTime))
    }

    val records = mutableListOf(
        PartOfDateTimeRange(startDate, startTime..LocalTime.MAX),
        PartOfDateTimeRange(endDate, LocalTime.MIN..endTime),
    )

    val numOfRecords = ChronoUnit.DAYS.between(start, endInclusive) - 1

    for (addedDays in 1..numOfRecords) {
        val date = startDate.plusDays(addedDays)
        records.add(1, PartOfDateTimeRange(date, LocalTime.MIN..LocalTime.MAX))
    }

    return records
}
