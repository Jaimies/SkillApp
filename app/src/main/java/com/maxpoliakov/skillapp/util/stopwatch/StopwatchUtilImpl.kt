package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUtilImpl @Inject constructor(
    private val persistence: StopwatchPersistence,
    private val addRecord: AddRecordUseCase,
    private val scope: CoroutineScope
) : StopwatchUtil {
    override val state: StateFlow<StopwatchState> get() = _state
    private val _state = MutableStateFlow(persistence.getState())

    override fun toggle(skillId: Int) {
        val state = _state.value
        if (state is Running && state.skillId == skillId) stop()
        else start(skillId)
    }

    private fun stop() = scope.launch {
        val state = _state.value
        if (state is Running) addRecord.run(Record("", state.skillId, state.time))
        setState(Paused)
    }

    private fun start(skillId: Int) {
        val startTime = getZonedDateTime()
        setState(Running(startTime, skillId))
    }

    private fun setState(state: StopwatchState) {
        _state.value = state
        persistence.saveState(state)
    }
}
