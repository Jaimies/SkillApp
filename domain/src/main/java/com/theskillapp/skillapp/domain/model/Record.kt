package com.theskillapp.skillapp.domain.model

import com.theskillapp.skillapp.shared.util.getCurrentDate
import java.time.LocalDate
import java.time.LocalTime

data class Record(
    val name: String,
    val skillId: Id,
    val count: Long,
    val unit: MeasurementUnit<*>,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val timeRange: ClosedRange<LocalTime>? = null,
)
