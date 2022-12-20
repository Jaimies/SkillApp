package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Paused
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.range.split
import com.maxpoliakov.skillapp.shared.util.toDuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchImpl @Inject constructor(
    private val persistence: StopwatchRepository,
    private val addRecord: AddRecordUseCase,
    private val skillRepository: SkillRepository,
    private val notificationUtil: NotificationUtil,
    private val clock: Clock,
) : Stopwatch {
    override val state: StateFlow<StopwatchState> get() = _state
    private val _state = MutableStateFlow(persistence.getState())

    init {
        updateNotification()
    }

    override fun updateNotification() {
        updateNotification(_state.value)
    }

    private fun updateNotification(state: StopwatchState) {
        if (state is Running) notificationUtil.showStopwatchNotification(state)
        else notificationUtil.removeStopwatchNotification()
    }

    override fun updateState() {
        _state.value = persistence.getState()
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
        val skill = skillRepository.getSkillById(skillId) ?: return null
        val state = Running(ZonedDateTime.now(clock), skillId, skill.groupId)
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
        updateNotification(state)
    }

    private suspend fun addRecord(state: Running): Record {
        val dateTimeRange = state.startTime.toLocalDateTime()..LocalDateTime.now(clock)

        for (range in dateTimeRange.split()) {
            val record = Record(
                name = "",
                skillId = state.skillId,
                count = range.toDuration().toMillis(),
                date = range.date,
                unit = MeasurementUnit.Millis,
                timeRange = range.range,
            )

            addRecord.run(record)
        }

        return Record(
            name = "",
            skillId = state.skillId,
            count = dateTimeRange.toDuration().toMillis(),
            date = state.startTime.toLocalDate(),
            unit = MeasurementUnit.Millis,
            timeRange = null,
        )
    }
}