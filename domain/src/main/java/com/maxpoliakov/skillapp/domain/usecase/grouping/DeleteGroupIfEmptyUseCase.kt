package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class DeleteGroupIfEmptyUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun run(groupId: Int) {
        if (groupId == -1) return

        val group = skillGroupRepository.getSkillGroupById(groupId)

        if (group != null && group.skills.isEmpty())
            skillGroupRepository.deleteGroup(group.id)
    }
}
