package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUtilImpl @Inject constructor(
    private val coroutineScope: CoroutineScope
) : StopwatchUtil {
    override val state: StateFlow<StopwatchState> get() = _state;
    private val _state = MutableStateFlow<StopwatchState>(Paused)

    override fun toggle(skillId: Int) {
        if (_state.value is Running) stop()
        else start(skillId)
    }

    private fun stop() {
        _state.value = Paused
    }

    private fun start(skillId: Int) = coroutineScope.launch {
        _state.value = Running(Duration.ZERO, skillId)
        while (true) {
            delay(1000)
            val state = _state.value
            if (state !is Running) break
            _state.value = state.copy(time = state.time.plusSeconds(1))
        }
    }
}
