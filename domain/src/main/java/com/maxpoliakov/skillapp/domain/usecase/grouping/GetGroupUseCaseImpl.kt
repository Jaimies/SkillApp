package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupUseCaseImpl @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
): GetGroupUseCase {
    override fun getById(id: Int): Flow<SkillGroup> {
        return skillGroupRepository.getSkillGroupFlowById(id)
    }
}
