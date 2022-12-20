package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.StopwatchState

interface StopwatchRepository {
    fun getState(): StopwatchState
    fun saveState(state: StopwatchState)
}
