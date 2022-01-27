package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.until
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.SkillChartData
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

class SkillDetailViewModel(
    private val addRecord: AddRecordUseCase,
    private val updateSkillUseCase: UpdateSkillUseCase,
    private val deleteSkill: DeleteSkillUseCase,
    private val ioScope: CoroutineScope,
    private val stopwatchUtil: StopwatchUtil,
    private val skillId: Int,
    getSkillById: GetSkillByIdUseCase,
    getStats: GetStatsUseCase
) : DetailsViewModel() {

    val showRecordDialog = SingleLiveEvent<Any>()
    private val _showRecordAdded = SingleLiveEvent<Record>()
    val showRecordAdded: LiveData<Record?> get() = _showRecordAdded

    val stopwatchIsRunning = stopwatchUtil.state.map {
        it is Running && it.skillId == skillId
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map {
        if (it is Running) it.startTime
        else getZonedDateTime()
    }.asLiveData()

    val skill = getSkillById.run(skillId).shareIn(viewModelScope, Eagerly, replay = 1)
    val skillLiveData = skill.asLiveData()

    val summary = skill.map { skill ->
        ProductivitySummary(skill.totalTime, skill.lastWeekTime)
    }.asLiveData()

    private val tick = flow {
        while (true) {
            emit(Unit)
            delay(GOAL_PROGRESS_REFRESH_INTERVAL)
        }
    }

    private val timeOnStopwatch = stopwatchUtil.state.combine(tick) { state, _ ->
        if (state !is Running) return@combine Duration.ZERO
        state.startTime.until(ZonedDateTime.now())
    }

    val chartData = SkillChartData(getStats, skillId)
    private val _timeToday = getStats.getTimeToday(skillId)
        .combine(timeOnStopwatch) { recordedTime, timeOnStopwatch ->
            recordedTime + timeOnStopwatch
        }

    val timeToday = _timeToday.asLiveData()

    val goalPercentage = combine(skill, _timeToday) { skill, timeToday ->
        val goalTime = skill.goal?.time ?: return@combine 0
        (timeToday.toMillis() * 100_000 / goalTime.toMillis()).toInt()
    }.asLiveData()

    override val nameFlow = skill.map { it.name }

    fun addRecord(time: Duration) {
        val record = Record("", skillId, time)
        ioScope.launch { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { deleteSkill.run(skill) }
        logEvent("delete_skill")
    }

    fun toggleTimer() = ioScope.launch {
        val record = stopwatchUtil.toggle(skillId)
        record.let(_showRecordAdded::postValue)
        logEvent("toggle_timer")
    }

    fun showRecordDialog() {
        showRecordDialog.call()
        logEvent("add_time_manually")
    }

    override suspend fun update(name: String) {
        updateSkillUseCase.updateName(skillId, name)
    }

    class Factory @Inject constructor(
        private val addRecord: AddRecordUseCase,
        private val updateSkillUseCase: UpdateSkillUseCase,
        private val getSkillById: GetSkillByIdUseCase,
        private val getStats: GetStatsUseCase,
        private val deleteSkill: DeleteSkillUseCase,
        private val ioScope: CoroutineScope,
        private val stopwatchUtil: StopwatchUtil,
    ) {
        fun create(skillId: Int): SkillDetailViewModel {
            return SkillDetailViewModel(
                addRecord,
                updateSkillUseCase,
                deleteSkill,
                ioScope,
                stopwatchUtil,
                skillId,
                getSkillById,
                getStats,
            )
        }
    }

    companion object {
        private const val GOAL_PROGRESS_REFRESH_INTERVAL = 2_000L
    }
}
