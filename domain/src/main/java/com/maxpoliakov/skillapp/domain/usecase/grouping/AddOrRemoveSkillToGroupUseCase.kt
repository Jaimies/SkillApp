package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class AddOrRemoveSkillToGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun createGroup(originalSkill: Skill, group: SkillGroup): Long {
        val newGroupId = skillGroupRepository.createGroup(group)
        deleteGroupIfEmpty(originalSkill.groupId)
        return newGroupId
    }

    suspend fun addToGroup(skill: Skill, groupId: Int) {
        skillGroupRepository.addSkillToGroup(skill.id, groupId)
        deleteGroupIfEmpty(skill.groupId)
    }

    suspend fun removeFromGroup(skill: Skill) {
        skillGroupRepository.removeSkillFromGroup(skill.id)
        deleteGroupIfEmpty(skill.groupId)
    }

    private suspend fun deleteGroupIfEmpty(groupId: Int) {
        if (groupId == -1) return

        val group = skillGroupRepository.getSkillGroupById(groupId)

        if (group != null && group.skills.isEmpty())
            skillGroupRepository.deleteGroup(group.id)
    }
}
