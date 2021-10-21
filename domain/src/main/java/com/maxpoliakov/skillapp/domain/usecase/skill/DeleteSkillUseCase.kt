package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.DeleteGroupIfEmptyUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class DeleteSkillUseCase @Inject constructor(
    private val deleteGroupIfEmpty: DeleteGroupIfEmptyUseCase,
    private val skillRepository: SkillRepository,
    private val stopwatchUtil: StopwatchUtil,
) {
    suspend fun run(skill: Skill) {
        val state = stopwatchUtil.state.value

        skillRepository.deleteSkill(skill)

        if (state is StopwatchState.Running && state.skillId == skill.id) {
            delay(100)
            stopwatchUtil.cancel()
        }

        deleteGroupIfEmpty.run(skill.groupId)
    }
}
