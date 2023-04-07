package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import javax.inject.Inject

class UpdateOrderUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val skillGroupRepository: SkillGroupRepository,
): UpdateOrderUseCase {
    override suspend fun run(items: List<Orderable>) {
        items
            .filter { it !is Skill || it.isNotInAGroup }
            .forEachIndexed { index, item ->
                if (item is Skill) skillRepository.updateOrder(item.id, index)
                if (item is SkillGroup) {
                    skillGroupRepository.updateOrder(item.id, index)

                    val skills = items.filterIsInstance<Skill>()
                        .filter { it.groupId == item.id }

                    skills.forEachIndexed { index, skill ->
                        skillRepository.updateOrder(skill.id, index)
                    }
                }
            }
    }

}
