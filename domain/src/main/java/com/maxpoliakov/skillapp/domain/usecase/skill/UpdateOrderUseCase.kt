package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val skillRepository: SkillRepository
) {
    suspend fun run(skills: List<Skill>) {
        skillRepository.updateOrder(skills)
    }
}