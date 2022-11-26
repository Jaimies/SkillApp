package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.LocalDate
import java.time.LocalDateTime

data class Record(
    val name: String,
    val skillId: Id,
    val count: Long,
    val unit: MeasurementUnit,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val dateTimeRange: ClosedRange<LocalDateTime>? = null
)
