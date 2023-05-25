package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Stopwatch {
    val state: Flow<State>

    suspend fun start(skillId: Int): StateChange
    suspend fun stop(skillId: Int): StateChange
    suspend fun cancel(skillId: Int)
    suspend fun toggle(skillId: Int): StateChange
//    fun updateNotification()
//    fun updateState()

    sealed class StateChange {
        open val addedRecords: List<Record> = listOf()

        object Start : StateChange()
        data class Stop(override val addedRecords: List<Record> = listOf()) : StateChange()
        object None : StateChange()
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

        fun getTimersForSkillIds(skillIds: List<Int>): List<Timer> {
            return skillIds.mapNotNull(this::getTimerForSkillId)
        }
    }
}
