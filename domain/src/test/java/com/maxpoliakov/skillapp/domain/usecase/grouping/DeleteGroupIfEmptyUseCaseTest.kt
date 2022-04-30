package com.maxpoliakov.skillapp.domain.usecase.grouping

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Duration

class DeleteGroupIfEmptyUseCaseTest : StringSpec({
    "deletes group if it becomes empty" {
        val (useCase, skillGroupRepository) = createUseCase(hasSkills = false)

        useCase.run(1)
        coVerify { skillGroupRepository.deleteGroup(1) }
    }

    "doesn't delete group if it still has skills" {
        val (useCase, skillGroupRepository) = createUseCase(hasSkills = true)

        useCase.run(1)
        coVerify(exactly = 0) { skillGroupRepository.deleteGroup(1) }
    }
})

private val skill = Skill("name", MeasurementUnit.Millis, Duration.ofHours(2).toMillis(), Duration.ofHours(1).toMillis(), goal = null)

private fun createUseCase(hasSkills: Boolean): Pair<DeleteGroupIfEmptyUseCase, SkillGroupRepository> {
    val skillGroupRepository = mockk<SkillGroupRepository>(relaxed = true)

    val skillGroup = SkillGroup(1, "name", if (hasSkills) listOf(skill) else listOf(), MeasurementUnit.Millis, null, -1)
    coEvery { skillGroupRepository.getSkillGroupById(any()) } returns skillGroup

    return DeleteGroupIfEmptyUseCase(skillGroupRepository) to skillGroupRepository
}
