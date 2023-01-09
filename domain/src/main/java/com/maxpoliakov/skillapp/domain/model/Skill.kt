package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.LocalDate

data class Skill(
    override val name: String,
    override val unit: MeasurementUnit,
    override val totalCount: Long,
    val initialCount: Long,
    override val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    override val goal: Goal? = null,
    override val order: Int = -1,
) : Trackable, Orderable
