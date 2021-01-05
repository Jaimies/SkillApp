package com.maxpoliakov.skillapp

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState

class StubStopwatchPersistence(
    private val state: StopwatchState
) : StopwatchPersistence {
    override fun getState() = state
    override fun saveState(state: StopwatchState) {}
}
