package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StubStopwatch @Inject constructor() : Stopwatch {
    private val startTime = ZonedDateTime.now().minusHours(1).minusMinutes(2)
    val timer = Timer(-1, 3, startTime)
    private val runningState = Stopwatch.State(timers = listOf(timer))

    private val _state = MutableStateFlow(runningState)
    override val state = _state.asStateFlow()

    override suspend fun start(skillId: Int) = StateChange.Start

    override suspend fun stop(skillId: Int): StateChange.Stop {
        _state.value = Stopwatch.State(timers = listOf())
        return StateChange.Stop()
    }

    override suspend fun cancel(skillId: Int) {}
    override suspend fun toggle(skillId: Int) = StateChange.Start

//    override fun updateNotification() {}
}
