package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUtilImpl @Inject constructor(
    private val persistence: StopwatchPersistence
) : StopwatchUtil {
    override val state: StateFlow<StopwatchState> get() = _state
    private val _state = MutableStateFlow(persistence.getState())

    override fun toggle(skillId: Int, callback: StopwatchCallback) {
        val state = _state.value
        if (state is Running && state.skillId == skillId) stop(callback)
        else start(skillId)
    }

    private fun stop(callback: StopwatchCallback) {
        val state = _state.value
        if (state is Running) callback.invoke(state.time)
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
