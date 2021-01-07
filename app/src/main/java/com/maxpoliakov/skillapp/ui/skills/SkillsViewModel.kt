package com.maxpoliakov.skillapp.ui.skills

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsUseCase
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SkillsViewModel @ViewModelInject constructor(
    getSkill: GetSkillByIdUseCase,
    getSkills: GetSkillsUseCase,
    private val stopwatchUtil: StopwatchUtil
) : ViewModel() {

    val skills = getSkills.run()
        .mapList { it.mapToPresentation() }
        .asLiveData()

    val isEmpty = skills.map { it.isEmpty() }

    val trackingSkill = stopwatchUtil.state.flatMapLatest { state ->
        if (state is Running) getSkill.run(state.skillId)
        else flowOf(null)
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map { state ->
        if (state is Running) state.startTime else getZonedDateTime()
    }.asLiveData()
    val isActive = stopwatchUtil.state.map { it is Running }.asLiveData()

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun stopTimer() = stopwatchUtil.stop()
    fun navigateToAddSkill() = navigateToAddEdit.call()
}
