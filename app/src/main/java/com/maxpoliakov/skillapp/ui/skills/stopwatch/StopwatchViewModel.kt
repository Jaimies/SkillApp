package com.maxpoliakov.skillapp.ui.skills.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StopwatchViewModel @Inject constructor(
    private val stopwatchUtil: StopwatchUtil,
    private val getSkill: GetSkillByIdUseCase,
    private val ioScope: CoroutineScope,
) {
    private val _navigateToSkill = SingleLiveEvent<Skill>()
    val navigateToSkill: LiveData<Skill> get() = _navigateToSkill
    private val _showRecordAdded = SingleLiveEvent<Record>()
    val showRecordAdded: LiveData<Record?> get() = _showRecordAdded

    val trackingSkill = stopwatchUtil.state.flatMapLatest { state ->
        if (state is StopwatchState.Running) getSkill.run(state.skillId)
        else flowOf(null)
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map { state ->
        if (state is StopwatchState.Running) state.startTime else getZonedDateTime()
    }.asLiveData()

    val isActive = stopwatchUtil.state.map { it is StopwatchState.Running }.asLiveData()

    fun stopTimer() = ioScope.launch {
        withContext(Dispatchers.Main) {
            val record = stopwatchUtil.stop().await()
            record?.let { record -> _showRecordAdded.value = record }
        }
    }

    fun navigateToCurrentlyTrackedSkill() {
        trackingSkill.value?.let { skill ->
            _navigateToSkill.value = skill
        }
    }
}
