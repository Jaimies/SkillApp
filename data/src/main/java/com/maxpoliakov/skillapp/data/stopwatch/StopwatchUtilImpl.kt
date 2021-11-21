package com.maxpoliakov.skillapp.data.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUtilImpl @Inject constructor(
    private val persistence: StopwatchPersistence,
    private val addRecord: AddRecordUseCase,
    private val notificationUtil: NotificationUtil,
    private val scope: CoroutineScope
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

    override fun toggle(skillId: Int): Deferred<Record?> = scope.async {
        val state = _state.value
        if (state is Running && state.skillId == skillId) return@async stop().await()

        start(skillId)
        return@async null
    }

    override fun stop(): Deferred<Record?> = scope.async {
        val state = _state.value

        if (state !is Running) return@async null
        setState(Paused)
        return@async addRecord(state)
    }

    override fun cancel() {
        setState(Paused)
    }

    override fun start(skillId: Int) {
        if (!shouldStartTimer(skillId)) return
        val state = Running(getZonedDateTime(), skillId)
        setState(state)
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
