package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.sumByLong

data class SkillGroup(
    override val id: Int,
    val name: String,
    val skills: List<Skill>,
    override val unit: MeasurementUnit,
    override val goal: Goal?,
    override val order: Int,
) : Trackable, Orderable {
    override val totalCount get() = skills.sumByLong(Skill::totalCount)
}
