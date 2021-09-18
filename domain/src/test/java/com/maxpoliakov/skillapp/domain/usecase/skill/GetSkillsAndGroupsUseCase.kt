package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import java.time.Duration

class GetSkillsAndSkillGroupsUseCaseTest : StringSpec({
    "getSkillsAndGroups()" {
        val useCase = createUseCase()

        useCase.getSkillsAndGroups().await() shouldBe SkillsAndGroups(
            skills = listOf(createSkill(-1)),
            groups = listOf(createGroup(), createGroup()),
        )
    }
})

private fun createUseCase(): GetSkillsAndSkillGroupsUseCase {
    val skillRepository = mockk<SkillRepository>(relaxed = true)
    val groupRepository = mockk<SkillGroupRepository>(relaxed = true)

    every { skillRepository.getSkills() } returns flowOf(listOf(createSkill(1), createSkill(2), createSkill(-1)))
    every { groupRepository.getSkillGroups() } returns flowOf(listOf(createGroup(), createGroup()))

    return GetSkillsAndSkillGroupsUseCase(skillRepository, groupRepository)
}

private fun createSkill(groupId: Int): Skill {
    return Skill("name", Duration.ZERO, Duration.ZERO, groupId = groupId)
}

private fun createGroup(): SkillGroup {
    return SkillGroup(1, "group", listOf(), -1)
}