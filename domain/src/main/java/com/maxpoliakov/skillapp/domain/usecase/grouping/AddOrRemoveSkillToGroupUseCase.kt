package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup

interface AddOrRemoveSkillToGroupUseCase {
    suspend fun createGroup(originalSkill: Skill, group: SkillGroup): Long
    suspend fun addToGroup(skill: Skill, groupId: Int)
    suspend fun removeFromGroup(skill: Skill)
}
