package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository

class StubStopwatchRepository(
    private val state: Stopwatch.State,
) : StopwatchRepository {
    override fun getState() = state
    override fun saveState(state: Stopwatch.State) {}
}
