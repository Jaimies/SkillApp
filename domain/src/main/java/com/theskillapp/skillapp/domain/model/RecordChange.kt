package com.theskillapp.skillapp.domain.model

import java.time.LocalDate
import java.time.LocalTime

interface RecordChange : Change<Record> {
    data class Date(val newDate: LocalDate) : RecordChange {
        override fun apply(record: Record): Record {
            return record.copy(date = newDate)
        }
    }

    data class Count(val newCount: Long) : RecordChange {
        override fun apply(record: Record): Record {
            return record.copy(count = newCount)
        }
    }

    data class TimeRange(val change: Change<ClosedRange<LocalTime>>) : RecordChange {
        override fun apply(record: Record): Record {
            if (record.timeRange == null) return record
            return record.copy(timeRange = change.apply(record.timeRange))
        }
    }
}
