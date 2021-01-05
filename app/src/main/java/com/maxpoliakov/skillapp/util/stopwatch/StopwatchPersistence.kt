package com.maxpoliakov.skillapp.util.stopwatch

interface StopwatchPersistence {
    fun getState(): StopwatchState
    fun saveState(state: StopwatchState)
}
