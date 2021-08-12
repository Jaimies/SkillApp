package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun updateName(groupId: Int, newName: String) {
        skillGroupRepository.updateGroupName(groupId, newName)
    }
}
