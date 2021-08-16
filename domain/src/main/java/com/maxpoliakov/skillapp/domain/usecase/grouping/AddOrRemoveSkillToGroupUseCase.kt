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

    suspend fun addToGroup(skillId: Int, groupId: Int) {
        skillGroupRepository.addSkillToGroup(skillId, groupId)
    }

    suspend fun removeFromGroup(skillId: Int) {
        skillGroupRepository.removeSkillFromGroup(skillId)
    }
}
