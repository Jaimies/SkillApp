package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime

interface Stopwatch {
    val state: StateFlow<State>

    suspend fun start(skillId: Int): StateChange.Start
    suspend fun stop(): StateChange.Stop
    fun cancel()
    suspend fun toggle(skillId: Int): StateChange
    fun updateNotification()
    fun updateState()

    sealed class StateChange {
        abstract val addedRecords: List<Record>

        data class Start(override val addedRecords: List<Record> = listOf()) : StateChange()
        data class Stop(override val addedRecords: List<Record> = listOf()) : StateChange()
    }

    sealed class State {
        data class Running(
            val startTime: ZonedDateTime,
            val skillId: Int,
            val groupId: Int,
        ) : State()

        object Paused : State()
    }
}
