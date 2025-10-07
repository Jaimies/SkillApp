package com.theskillapp.skillapp.domain.usecase.grouping

import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup

interface AddOrRemoveSkillToGroupUseCase {
    suspend fun createGroup(originalSkill: Skill, group: SkillGroup): Long
    suspend fun addToGroup(skill: Skill, groupId: Int)
    suspend fun removeFromGroup(skill: Skill)
}
