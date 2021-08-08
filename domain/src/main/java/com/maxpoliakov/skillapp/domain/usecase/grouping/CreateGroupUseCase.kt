package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val skillGroupRepository: SkillGroupRepository,
) {
    suspend fun run(name: String, skills: List<Skill>) {
        skillGroupRepository.createGroup(name, skills)
    }
}
