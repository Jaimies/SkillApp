package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository

class StubStopwatchRepository(
    private val state: StopwatchState,
) : StopwatchRepository {
    override fun getState() = state
    override fun saveState(state: StopwatchState) {}
}
