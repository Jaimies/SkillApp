package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import kotlinx.coroutines.flow.StateFlow

interface Stopwatch {
    val state: StateFlow<StopwatchState>

    suspend fun start(skillId: Int): List<Record>
    suspend fun stop(): List<Record>
    fun cancel()
    suspend fun toggle(skillId: Int): List<Record>
    fun updateNotification()
    fun updateState()
}
