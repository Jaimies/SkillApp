package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.domain.model.StopwatchState

interface StopwatchPersistence {
    fun getState(): StopwatchState
    fun saveState(state: StopwatchState)
}
