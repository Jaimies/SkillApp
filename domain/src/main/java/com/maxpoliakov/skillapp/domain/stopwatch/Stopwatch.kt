package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Timer
import kotlinx.coroutines.flow.StateFlow

interface Stopwatch {
    val state: StateFlow<State>

    suspend fun start(skillId: Int): StateChange
    suspend fun stop(): StateChange
    fun cancel()
    suspend fun toggle(skillId: Int): StateChange
    fun updateNotification()
    fun updateState()

    sealed class StateChange {
        abstract val addedRecords: List<Record>

        data class Start(override val addedRecords: List<Record> = listOf()) : StateChange()
        data class Stop(override val addedRecords: List<Record> = listOf()) : StateChange()

        object None : StateChange() {
            override val addedRecords = listOf<Record>()
        }
    }

    data class State(val timers: List<Timer>) {
        fun hasActiveTimers(): Boolean {
            return timers.isNotEmpty()
        }

        fun hasTimerForSkillId(skillId: Int) : Boolean {
            return timers.any { it.skillId == skillId }
        }

        fun getTimerForSkillId(skillId: Int): Timer? {
            return timers.find { it.skillId == skillId }
        }

        fun getTimersForGroupId(groupId: Int): List<Timer> {
            return timers.filter { it.groupId == groupId }
        }
    }
}
