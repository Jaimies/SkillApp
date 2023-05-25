package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.grouping.DeleteGroupIfEmptyUseCaseImpl
import javax.inject.Inject

class ManageSkillUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val deleteGroupIfEmpty: DeleteGroupIfEmptyUseCaseImpl
): ManageSkillUseCase {
    override suspend fun addSkill(skill: Skill) = skillRepository.addSkill(skill)

    override suspend fun updateSkill(skillId: Int, newName: String, newGoal: Goal?) {
        skillRepository.updateName(skillId, newName)
        skillRepository.updateGoal(skillId, newGoal)
    }

    override suspend fun deleteSkill(skill: Skill) {
        skillRepository.deleteSkill(skill)
        deleteGroupIfEmpty.run(skill.groupId)
    }
}
