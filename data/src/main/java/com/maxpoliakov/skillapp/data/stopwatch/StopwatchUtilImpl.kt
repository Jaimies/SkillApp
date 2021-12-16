package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUtilImpl @Inject constructor(
    private val persistence: StopwatchPersistence,
    private val addRecord: AddRecordUseCase,
    private val notificationUtil: NotificationUtil,
) : StopwatchUtil {
    override val state: StateFlow<StopwatchState> get() = _state
    private val _state = MutableStateFlow(persistence.getState())

    init {
        updateNotification()
    }

    override fun updateNotification() {
        val state = _state.value
        if (state is Running) notificationUtil.showStopwatchNotification(state)
        else notificationUtil.removeStopwatchNotification()
    }

    override suspend fun toggle(skillId: Int): Record? {
        val state = _state.value
        if (state is Running && state.skillId == skillId) return stop()

        return start(skillId)
    }

    override suspend fun stop(): Record? {
        val state = _state.value

        if (state !is Running) return null
        setState(Paused)
        return addRecord(state)
    }

    override fun cancel() {
        setState(Paused)
    }

    override suspend fun start(skillId: Int): Record? {
        if (!shouldStartTimer(skillId)) return null
        val record = addRecordIfNeeded(_state.value)
        val state = Running(getZonedDateTime(), skillId)
        setState(state)

        return record
    }

    private suspend fun addRecordIfNeeded(state: StopwatchState): Record? {
        if (state is Running)
            return addRecord(state)

        return null
    }

    private fun shouldStartTimer(skillId: Int): Boolean {
        val state = this.state.value
        return state !is Running || state.skillId != skillId
    }

    private fun setState(state: StopwatchState) {
        _state.value = state
        persistence.saveState(state)
        updateNotification()
    }

    private suspend fun addRecord(state: Running): Record {
        val record = Record("", state.skillId, state.time)
        val recordId = addRecord.run(record)
        return record.copy(id = recordId.toInt())
    }
}
