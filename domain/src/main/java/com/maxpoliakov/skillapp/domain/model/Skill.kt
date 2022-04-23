package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.LocalDate

data class Skill(
    val name: String,
    val unit: MeasurementUnit,
    val totalCount: Long,
    val initialCount: Long,
    val lastWeekCount: Long = 0,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    val goal: Goal? = null,
    override val order: Int = -1,
) : Orderable
