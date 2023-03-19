package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class StopwatchViewModel @Inject constructor(
    private val stopwatch: Stopwatch,
    private val getSkill: GetSkillByIdUseCase,
    @ApplicationScope
    private val scope: CoroutineScope,
) {
    private val _navigateToSkill = SingleLiveEvent<Skill>()
    val navigateToSkill: LiveData<Skill> get() = _navigateToSkill
    private val _showRecordAdded = SingleLiveEvent<Record>()
    val showRecordAdded: LiveData<Record?> get() = _showRecordAdded

    val trackingSkill = stopwatch.state.flatMapLatest { state ->
        if (state is StopwatchState.Running) getSkill.run(state.skillId)
        else flowOf(null)
    }.asLiveData()

    val stopwatchStartTime = stopwatch.state.map { state ->
        if (state is StopwatchState.Running) state.startTime else getZonedDateTime()
    }.asLiveData()

    val isActive = stopwatch.state.map { it is StopwatchState.Running }.asLiveData()

    fun stopTimer() = scope.launch {
        val record = stopwatch.stop()
        record?.let(_showRecordAdded::postValue)
    }

    fun navigateToCurrentlyTrackedSkill() {
        trackingSkill.value?.let { skill ->
            _navigateToSkill.value = skill
        }
    }
}