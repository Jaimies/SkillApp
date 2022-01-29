package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class UpdateSkillUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    suspend fun updateSkill(skillId: Int, newName: String, newGoal: Goal?) {
        skillRepository.updateName(skillId, newName)
        skillRepository.updateGoal(skillId, newGoal)
    }
}
