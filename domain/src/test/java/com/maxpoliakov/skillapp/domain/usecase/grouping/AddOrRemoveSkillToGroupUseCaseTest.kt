package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Duration

class AddOrRemoveSkillToGroupUseCaseTest : StringSpec({
    "deletes group if it becomes empty" {
        val (useCase, skillGroupRepository) = createUseCase()
        val skill = Skill("skill", Duration.ZERO, Duration.ZERO, groupId = 1)

        useCase.addToGroup(skill, 1)
        coVerify { skillGroupRepository.deleteGroup(1) }
    }

    "doesn't delete group if it still has skills" {
        val (useCase, skillGroupRepository) = createUseCase()
        val skill = Skill("skill", Duration.ZERO, Duration.ZERO, groupId = -1)

        useCase.addToGroup(skill, 1)
        coVerify(exactly = 0) { skillGroupRepository.deleteGroup(1) }
    }
})

private fun createUseCase(): Pair<AddOrRemoveSkillToGroupUseCase, SkillGroupRepository> {
    val skillGroupRepository = mockk<SkillGroupRepository>(relaxed = true)

    coEvery { skillGroupRepository.getSkillGroupById(any()) } returns SkillGroup(1, "name", listOf(), -1)

    return AddOrRemoveSkillToGroupUseCase(skillGroupRepository) to skillGroupRepository
}
