package com.theskillapp.skillapp.domain.usecase.grouping

import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class UpdateGroupUseCaseImpl @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
): UpdateGroupUseCase {
    override suspend fun update(groupId: Int, newName: String, newGoal: Goal?) {
        skillGroupRepository.updateGroupName(groupId, newName)
        skillGroupRepository.updateGoal(groupId, newGoal)
    }
}
