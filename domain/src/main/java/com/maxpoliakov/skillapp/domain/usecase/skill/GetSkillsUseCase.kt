package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class GetSkillsUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    fun run() = skillRepository.getSkills()
}
