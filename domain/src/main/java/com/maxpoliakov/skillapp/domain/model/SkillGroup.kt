package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.sumByLong

data class SkillGroup(
    val id: Int,
    val name: String,
    val skills: List<Skill>,
    val unit: MeasurementUnit,
    val goal: Goal?,
    override val order: Int,
) : Orderable {
    val totalCount get() = skills.sumByLong(Skill::totalCount)
    val lastWeekCount get() = skills.sumByLong(Skill::lastWeekCount)
}
