package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch.StateChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StubStopwatch @Inject constructor() : Stopwatch {
    private val _state = MutableStateFlow(Stopwatch.State(timers = listOf()))
    override val state = _state.asStateFlow()

    fun setState(state: Stopwatch.State) {
        _state.value = state
    }

    override suspend fun start(skillId: Int) = StateChange.Start

    override suspend fun stop(skillId: Int): StateChange.Stop {
        _state.value = Stopwatch.State(timers = listOf())
        return StateChange.Stop()
    }

    override suspend fun cancel(skillId: Int) {}
    override suspend fun toggle(skillId: Int) = StateChange.Start

    override suspend fun updateNotification() {}
}
