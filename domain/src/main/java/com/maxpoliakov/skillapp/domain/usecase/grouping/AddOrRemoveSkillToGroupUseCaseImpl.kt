package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class AddOrRemoveSkillToGroupUseCaseImpl @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
    private val deleteGroupIfEmpty: DeleteGroupIfEmptyUseCaseImpl,
): AddOrRemoveSkillToGroupUseCase {
    override suspend fun createGroup(originalSkill: Skill, group: SkillGroup): Long {
        val newGroupId = skillGroupRepository.createGroup(group)
        deleteGroupIfEmpty.run(originalSkill.groupId)
        return newGroupId
    }

    override suspend fun addToGroup(skill: Skill, groupId: Int) {
        skillGroupRepository.addSkillToGroup(skill.id, groupId)
        deleteGroupIfEmpty.run(skill.groupId)
    }

    override suspend fun removeFromGroup(skill: Skill) {
        skillGroupRepository.removeSkillFromGroup(skill.id)
        deleteGroupIfEmpty.run(skill.groupId)
    }
}
