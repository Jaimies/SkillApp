package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        scope.launch {
            _state.collect { updateNotification() }
        }
    }

    override fun updateNotification() {
        val state = _state.value
        if (state is Running) notificationUtil.showStopwatchNotification(state)
        else notificationUtil.removeStopwatchNotification()
    }

    override fun toggle(skillId: Int) {
        val state = _state.value
        if (state is Running && state.skillId == skillId) stop()
        else start(skillId)
    }

    override fun stop() {
        setState(Paused)
    }

    override fun start(skillId: Int) {
        val state = Running(getZonedDateTime(), skillId)
        setState(state)
    }

    private fun setState(state: StopwatchState) {
        addRecordIfNeeded()
        _state.value = state
        persistence.saveState(state)
    }

    private fun addRecordIfNeeded() {
        val state = _state.value
        if (state is Running) addRecord(state)
    }

    private fun addRecord(state: Running) = scope.launch {
        addRecord.run(Record("", state.skillId, state.time))
    }
}
