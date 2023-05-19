package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
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
        if (state.hasActiveTimers()) notificationUtil.showStopwatchNotification(state.timers.first())
        else notificationUtil.removeStopwatchNotification()
    }

    override fun updateState() {
        _state.value = persistence.getState()
    }

    override suspend fun toggle(skillId: Int): StateChange {
        val state = _state.value
        if (state.hasTimerForSkillId(skillId)) return stop()

        return start(skillId)
    }

    override suspend fun stop(): StateChange {
        val state = _state.value

        if (!state.hasActiveTimers()) return StateChange.None
        setState(Stopwatch.State(timers = listOf()))
        val addedRecords = addRecords(state.timers.first())
        return StateChange.Stop(addedRecords)
    }

    override fun cancel() {
        setState(Stopwatch.State(timers = listOf()))
    }

    override suspend fun start(skillId: Int): StateChange {
        if (_state.value.hasTimerForSkillId(skillId)) return StateChange.None
        val records = addRecordsIfNeeded(_state.value)
        val skill = skillRepository.getSkillById(skillId) ?: return StateChange.Start()
        val timer = Timer(ZonedDateTime.now(clock), skillId, skill.groupId)
        setState(Stopwatch.State(listOf(timer)))

        return StateChange.Start(records)
    }

    private suspend fun addRecordsIfNeeded(state: Stopwatch.State): List<Record> {
        if (state.hasActiveTimers())
            return addRecords(state.timers.first())

        return listOf()
    }

    private fun setState(state: Stopwatch.State) {
        _state.value = state
        persistence.saveState(state)
        updateNotification(state)
    }

    private suspend fun addRecords(timer: Timer): List<Record> {
        val dateTimeRange = timer.startTime.toLocalDateTime()..LocalDateTime.now(clock)

        val addedRecords = mutableListOf<Record>()

        for (range in dateTimeRange.split()) {
            val record = Record(
                name = "",
                skillId = timer.skillId,
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
