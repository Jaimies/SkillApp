package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.State.Running
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.State.Paused
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.shared.range.split
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
    override val state: StateFlow<Stopwatch.State> get() = _state
    private val _state = MutableStateFlow(persistence.getState())

    init {
        updateNotification()
    }

    override fun updateNotification() {
        updateNotification(_state.value)
    }

    private fun updateNotification(state: Stopwatch.State) {
        if (state is Running) notificationUtil.showStopwatchNotification(state)
        else notificationUtil.removeStopwatchNotification()
    }

    override fun updateState() {
        _state.value = persistence.getState()
    }

    override suspend fun toggle(skillId: Int): List<Record> {
        val state = _state.value
        if (state is Running && state.skillId == skillId) return stop()

        return start(skillId)
    }

    override suspend fun stop(): List<Record> {
        val state = _state.value

        if (state !is Running) return listOf()
        setState(Paused)
        return addRecords(state)
    }

    override fun cancel() {
        setState(Paused)
    }

    override suspend fun start(skillId: Int): List<Record> {
        if (!shouldStartTimer(skillId)) return listOf()
        val records = addRecordsIfNeeded(_state.value)
        val skill = skillRepository.getSkillById(skillId) ?: return listOf()
        val state = Running(ZonedDateTime.now(clock), skillId, skill.groupId)
        setState(state)

        return records
    }

    private suspend fun addRecordsIfNeeded(state: Stopwatch.State): List<Record> {
        if (state is Running)
            return addRecords(state)

        return listOf()
    }

    private fun shouldStartTimer(skillId: Int): Boolean {
        val state = this.state.value
        return state !is Running || state.skillId != skillId
    }

    private fun setState(state: Stopwatch.State) {
        _state.value = state
        persistence.saveState(state)
        updateNotification(state)
    }

    private suspend fun addRecords(state: Running): List<Record> {
        val dateTimeRange = state.startTime.toLocalDateTime()..LocalDateTime.now(clock)

        val addedRecords = mutableListOf<Record>()

        for (range in dateTimeRange.split()) {
            val record = Record(
                name = "",
                skillId = state.skillId,
                count = range.toDuration().toMillis(),
                date = range.date,
                unit = MeasurementUnit.Millis,
                timeRange = range.range,
            )

            val recordId = addRecord.run(record).toInt()
            addedRecords.add(record.copy(id = recordId))
        }

        return addedRecords
    }
}
