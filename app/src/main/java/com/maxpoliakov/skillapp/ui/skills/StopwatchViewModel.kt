package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StopwatchViewModel @Inject constructor(
    private val stopwatchUtil: StopwatchUtil,
    private val getSkill: GetSkillByIdUseCase,
    private val navigationUtil: NavigationUtil,
) {
    val trackingSkill = stopwatchUtil.state.flatMapLatest { state ->
        if (state is StopwatchState.Running) getSkill.run(state.skillId)
        else flowOf(null)
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map { state ->
        if (state is StopwatchState.Running) state.startTime else getZonedDateTime()
    }.asLiveData()

    val isActive = stopwatchUtil.state.map { it is StopwatchState.Running }.asLiveData()

    fun stopTimer() = stopwatchUtil.stop()

    fun navigateToCurrentlyTrackedSkill() {
        navigationUtil.navigateToSkillDetail(trackingSkill.value!!.id)
    }
}
