package com.maxpoliakov.skillapp.domain.usecase

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import java.time.Duration

fun createSkill(id: Int = -1, groupId: Int): Skill {
    return Skill("name", Duration.ZERO, Duration.ZERO, id = id, groupId = groupId, goal = null)
}

fun createGroup(id: Int = -1, skills: List<Skill> = listOf()): SkillGroup {
    return SkillGroup(id, "group", skills, -1)
}
