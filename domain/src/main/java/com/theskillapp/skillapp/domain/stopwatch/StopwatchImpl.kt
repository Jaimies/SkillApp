package com.theskillapp.skillapp.domain.stopwatch

import com.theskillapp.skillapp.domain.di.ApplicationScope
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.repository.NotificationUtil
import com.theskillapp.skillapp.domain.repository.TimerRepository
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch.StateChange
import com.theskillapp.skillapp.domain.usecase.records.AddRecordUseCase
import com.theskillapp.skillapp.shared.range.split
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchImpl @Inject constructor(
    private val timerRepository: TimerRepository,
    private val preferenceRepository: UserPreferenceRepository,
    private val addRecord: AddRecordUseCase,
    private val notificationUtil: NotificationUtil,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val clock: Clock,
) : Stopwatch {

    override val state = timerRepository.getAll().map(Stopwatch::State)

    init {
        scope.launch {
            state.collect { state ->
                notificationUtil.updateTimerNotifications(state.timers)
            }
        }
    }

    override suspend fun toggle(skillId: Int): StateChange {
        val state = state.first()
        if (state.hasTimerForSkillId(skillId)) return stop(skillId)

        return start(skillId)
    }

    override suspend fun stop(skillId: Int): StateChange {
        val timer = state.first().getTimerForSkillId(skillId) ?: return StateChange.None
        val addedRecords = addRecords(timer)
        timerRepository.remove(timer)
        return StateChange.Stop(addedRecords)
    }

    override suspend fun cancel(skillId: Int) {
        val timer = state.first().getTimerForSkillId(skillId) ?: return
        timerRepository.remove(timer)
    }

    override suspend fun start(skillId: Int): StateChange {
        if (state.first().hasTimerForSkillId(skillId)) return StateChange.None
        timerRepository.add(Timer(skillId, ZonedDateTime.now(clock)))
        return StateChange.Start
    }

    private suspend fun addRecords(timer: Timer): List<Record> {
        val dateTimeRange = timer.startTime.toLocalDateTime()..LocalDateTime.now(clock)

        val addedRecords = mutableListOf<Record>()

        for (range in dateTimeRange.split(preferenceRepository.getDayStartTime())) {
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

    override suspend fun updateNotification() {
        notificationUtil.updateTimerNotifications(state.first().timers)
    }
}
