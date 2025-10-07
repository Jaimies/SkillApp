package com.theskillapp.skillapp.domain.model

import com.theskillapp.skillapp.shared.util.getCurrentDate
import java.time.LocalDate

data class Skill(
    override val name: String,
    override val unit: MeasurementUnit<*>,
    override val totalCount: Long,
    val initialCount: Long,
    override val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    override val goal: Goal? = null,
    override val order: Int = -1,
) : Trackable, Orderable {
    val isInAGroup get() = this.groupId != -1
    val isNotInAGroup get() = this.groupId == -1

    fun canBeInGroupWith(other: Skill) = unit == other.unit
}
