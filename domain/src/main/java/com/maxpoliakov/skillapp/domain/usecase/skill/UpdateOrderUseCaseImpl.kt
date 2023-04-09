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
) : UpdateOrderUseCase {
    override suspend fun run(items: List<Orderable>) {
        items
            .filterNot { it is Skill && it.isInAGroup }
            .forEachIndexed { index, item -> updateOrder(index, item, items) }
    }

    private suspend fun updateOrder(index: Int, item: Orderable, allItems: List<Orderable>) {
        when (item) {
            is Skill -> updateOrderForSkill(index, item)
            is SkillGroup -> updateOrderForGroup(index, item, allItems)
        }
    }

    private suspend fun updateOrderForSkill(index: Int, skill: Skill) {
        skillRepository.updateOrder(skill.id, index)
    }

    private suspend fun updateOrderForGroup(index: Int, group: SkillGroup, allItems: List<Orderable>) {
        skillGroupRepository.updateOrder(group.id, index)

        val skills = allItems
            .filterIsInstance<Skill>()
            .filter { it.groupId == group.id }

        skills.forEachIndexed { index, skill ->
            skillRepository.updateOrder(skill.id, index)
        }
    }
}
