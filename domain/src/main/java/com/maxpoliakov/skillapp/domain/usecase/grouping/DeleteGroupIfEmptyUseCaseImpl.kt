package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class DeleteGroupIfEmptyUseCaseImpl @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
): DeleteGroupIfEmptyUseCase {
    override suspend fun run(groupId: Int) {
        if (groupId == -1) return

        val group = skillGroupRepository.getSkillGroupById(groupId)

        if (group != null && group.skills.isEmpty())
            skillGroupRepository.deleteGroup(group.id)
    }
}
