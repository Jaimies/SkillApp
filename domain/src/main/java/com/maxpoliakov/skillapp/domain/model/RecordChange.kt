package com.maxpoliakov.skillapp.domain.model

import java.time.LocalDate
import java.time.LocalTime

object RecordChange {
    data class Date(val newDate: LocalDate) : Change<Record> {
        override fun apply(record: Record): Record {
            return record.copy(date = newDate)
        }
    }

    data class Count(val newCount: Long) : Change<Record> {
        override fun apply(record: Record): Record {
            return record.copy(count = newCount)
        }
    }

    data class TimeRange(val change: Change<ClosedRange<LocalTime>>) : Change<Record> {
        override fun apply(record: Record): Record {
            if (record.timeRange == null) return record
            return record.copy(timeRange = change.apply(record.timeRange))
        }
    }
}
