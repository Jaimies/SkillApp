package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class GetSkillByIdUseCase @Inject constructor(private val skillRepository: SkillRepository) {
    fun run(id: Id) = skillRepository.getSkillFlowById(id)
}
