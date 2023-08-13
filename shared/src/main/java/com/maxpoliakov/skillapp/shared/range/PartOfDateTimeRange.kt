package com.maxpoliakov.skillapp.shared.range

import com.maxpoliakov.skillapp.shared.util.toDuration
import java.time.LocalDate
import java.time.LocalTime

data class PartOfDateTimeRange(val date: LocalDate, val range: ClosedRange<LocalTime>) {
    fun toDuration() = range.toDuration().let { duration ->
        if (duration.nano == 999_999_999) duration.plusNanos(1)
        else duration
    }
}
