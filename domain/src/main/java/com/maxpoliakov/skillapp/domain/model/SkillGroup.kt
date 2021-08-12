package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.sumByDuration

data class SkillGroup(
    val id: Int,
    val name: String,
    val skills: List<Skill>,
) {
    val totalTime get() = skills.sumByDuration(Skill::totalTime)
    val lastWeekTime get() = skills.sumByDuration(Skill::lastWeekTime)
}
