package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
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
) : DetailsViewModel(), ChartData {

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

    val dailyStats = getStats.getDailyStats(skillId).map { stats ->
        stats.withMissingStats(ChronoUnit.DAYS, LocalDate.now()).toEntries(ChronoUnit.DAYS)
    }.asLiveData()

    val weeklyStats = getStats.getWeeklyStats(skillId).map { stats ->
        stats.withMissingStats(ChronoUnit.WEEKS, LocalDate.now().atStartOfWeek()).toEntries(ChronoUnit.WEEKS)
    }.asLiveData()

    val monthlyStats = getStats.getMonthlyStats(skillId).map { stats ->
        stats.withMissingStats(ChronoUnit.MONTHS, LocalDate.now().withDayOfMonth(1)).toEntries(ChronoUnit.MONTHS)
    }.asLiveData()

    val summary = skill.map { skill ->
        ProductivitySummary(skill.totalTime, skill.lastWeekTime)
    }.asLiveData()

    private val _statisticType = MutableLiveData(ChronoUnit.DAYS)
    override val statisticType: LiveData<ChronoUnit> get() = _statisticType

    override val nameFlow = skill.map { it.name }

    override fun setStatisticType(type: ChronoUnit) {
        _statisticType.value = type
    }

    fun addRecord(time: Duration) {
        val record = Record("", skillId, time)
        ioScope.launch { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { deleteSkill.run(skill) }
        logEvent("delete_skill")
    }

    fun toggleTimer() = viewModelScope.launch {
        val record = stopwatchUtil.toggle(skillId).await()
        record.let { record -> _showRecordAdded.value = record }
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
        private val stopwatchUtil: StopwatchUtil
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
                getStats
            )
        }
    }
}

interface ChartData {
    val statisticType: LiveData<ChronoUnit>

    fun setStatisticType(type: ChronoUnit)
}
