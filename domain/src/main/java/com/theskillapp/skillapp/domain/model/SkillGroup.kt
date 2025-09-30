package com.theskillapp.skillapp.domain.model

import com.theskillapp.skillapp.shared.util.sumByLong

data class SkillGroup(
    override val id: Int,
    override val name: String,
    val skills: List<Skill>,
    override val unit: MeasurementUnit<*>,
    override val goal: Goal?,
    override val order: Int,
) : Trackable, Orderable {
    override val totalCount get() = skills.sumByLong(Skill::totalCount)
}
