package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StubTimerRepository(initialValue: List<Timer>): TimerRepository {
    private val _timers = MutableStateFlow(initialValue)
    private val timers = _timers.asStateFlow()

    override fun getAll() = timers

    override suspend fun add(timer: Timer) {
        _timers.emit(_timers.value + timer)
    }

    override suspend fun remove(timer: Timer) {
        _timers.emit(_timers.value.filterNot { it == timer })
    }
}
