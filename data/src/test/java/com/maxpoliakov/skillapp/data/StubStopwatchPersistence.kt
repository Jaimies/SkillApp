package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.domain.model.StopwatchState

class StubStopwatchPersistence(
    private val state: StopwatchState
) : StopwatchPersistence {
    override fun getState() = state
    override fun saveState(state: StopwatchState) {}
}
