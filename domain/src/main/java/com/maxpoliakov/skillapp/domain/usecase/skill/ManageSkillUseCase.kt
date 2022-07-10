package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.DeleteGroupIfEmptyUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ManageSkillUseCase @Inject constructor(
    private val skillRepository: SkillRepository,
    private val stopwatchUtil: StopwatchUtil,
    private val deleteGroupIfEmpty: DeleteGroupIfEmptyUseCase
) {
    suspend fun addSkill(skill: Skill) = skillRepository.addSkill(skill)

    suspend fun updateSkill(skillId: Int, newName: String, newGoal: Goal?) {
        skillRepository.updateName(skillId, newName)
        skillRepository.updateGoal(skillId, newGoal)
    }

    suspend fun deleteSkill(skill: Skill) {
        val state = stopwatchUtil.state.value

        skillRepository.deleteSkill(skill)

        if (state is StopwatchState.Running && state.skillId == skill.id) {
            delay(100)
            stopwatchUtil.cancel()
        }

        deleteGroupIfEmpty.run(skill.groupId)
    }
}
