package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.repository.SkillGroupRepository
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.usecase.createGroup
import com.theskillapp.skillapp.domain.usecase.createSkill
import io.kotest.core.spec.style.StringSpec
import io.mockk.coVerifyAll
import io.mockk.mockk

class UpdateOrderUseCaseTest : StringSpec({
    "works" {
        val skillRepository = mockk<SkillRepository>(relaxed = true)
        val groupRepository = mockk<SkillGroupRepository>(relaxed = true)

        val useCase = UpdateOrderUseCaseImpl(skillRepository, groupRepository)

        val items = listOf(
            createSkill(id = 1, groupId = -1),
            createSkill(id = 2, groupId = 1),
            createSkill(id = 3, groupId = 2),
            createSkill(id = 4, groupId = -1),
            createSkill(id = 5, groupId = 1),
            createGroup(
                id = 1,
                skills = listOf(),
            ),
            createGroup(
                id = 2,
                skills = listOf(),
            ),
        )

        useCase.run(items)

        coVerifyAll {
            skillRepository.updateOrder(1, 0)
            skillRepository.updateOrder(4, 1)

            groupRepository.updateOrder(1, 2)
            groupRepository.updateOrder(2, 3)

            skillRepository.updateOrder(2, 0)
            skillRepository.updateOrder(5, 1)
            skillRepository.updateOrder(3, 0)
        }
    }
})
