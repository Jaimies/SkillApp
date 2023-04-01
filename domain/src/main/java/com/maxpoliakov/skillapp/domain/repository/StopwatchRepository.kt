package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch

interface StopwatchRepository {
    fun getState(): Stopwatch.State
    fun saveState(state: Stopwatch.State)
}
