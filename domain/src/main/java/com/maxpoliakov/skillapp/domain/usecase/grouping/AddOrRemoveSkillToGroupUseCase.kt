package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class AddOrRemoveSkillToGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun createGroup(name: String, skills: List<Skill>) {
        skillGroupRepository.createGroup(name, skills)
    }

    suspend fun addToGroup(skill: Skill, groupId: Int) {
        skillGroupRepository.addSkillToGroup(skill.id, groupId)
        deleteGroupIfEmpty(skill)

    }

    suspend fun removeFromGroup(skill: Skill) {
        skillGroupRepository.removeSkillFromGroup(skill.id)
        deleteGroupIfEmpty(skill)
    }

    private suspend fun deleteGroupIfEmpty(skill: Skill) {
        if (skill.groupId == -1) return

        val group = skillGroupRepository.getSkillGroupById(skill.groupId)

        if (group.skills.isEmpty())
            skillGroupRepository.deleteGroup(group.id)
    }
}
