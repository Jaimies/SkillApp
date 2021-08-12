package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    fun getById(id: Int): Flow<SkillGroup> {
        return skillGroupRepository.getSkillGroupById(id)
    }
}
