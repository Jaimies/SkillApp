package com.maxpoliakov.skillapp.domain.usecase

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup

fun createSkill(id: Int = -1, groupId: Int): Skill {
    return Skill("name", MeasurementUnit.Millis, 0, 0, id = id, groupId = groupId, goal = null)
}

fun createGroup(id: Int = -1, skills: List<Skill> = listOf()): SkillGroup {
    return SkillGroup(id, "group", skills, MeasurementUnit.Millis, null, -1)
}
