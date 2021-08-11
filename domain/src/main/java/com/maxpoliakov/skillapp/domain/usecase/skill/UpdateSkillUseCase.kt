package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class UpdateSkillUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    suspend fun updateName(skillId: Int, newName: String) {
        skillRepository.updateName(skillId, newName)
    }
}
