package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class UpdateGroupUseCaseImpl @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
): UpdateGroupUseCase {
    override suspend fun update(groupId: Int, newName: String, newGoal: Goal?) {
        skillGroupRepository.updateGroupName(groupId, newName)
        skillGroupRepository.updateGoal(groupId, newGoal)
    }
}
