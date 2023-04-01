package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime

interface Stopwatch {
    val state: StateFlow<State>

    suspend fun start(skillId: Int): List<Record>
    suspend fun stop(): List<Record>
    fun cancel()
    suspend fun toggle(skillId: Int): List<Record>
    fun updateNotification()
    fun updateState()

    sealed class State {
        data class Running(
            val startTime: ZonedDateTime,
            val skillId: Int,
            val groupId: Int,
        ) : State()

        object Paused : State()
    }
}
