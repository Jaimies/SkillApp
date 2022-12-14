package com.maxpoliakov.skillapp.shared.util.range

import com.maxpoliakov.skillapp.shared.util.toDuration
import java.time.LocalDate
import java.time.LocalTime

class PartOfDateTimeRange(val date: LocalDate, val range: ClosedRange<LocalTime>) {
    fun toDuration() = range.toDuration().let { duration ->
        if (range.endInclusive == LocalTime.MAX) duration.plusMillis(1)
        else duration
    }
}
