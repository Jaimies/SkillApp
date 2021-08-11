package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class AddSkillToGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun run(skillId: Int, groupId: Int) {
        skillGroupRepository.addSkillToGroup(skillId, groupId)
    }
}
