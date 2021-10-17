package com.maxpoliakov.skillapp.ui.skilldetail

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
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.Duration
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

    val stopwatchIsRunning = stopwatchUtil.state.map {
        it is Running && it.skillId == skillId
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map {
        if (it is Running) it.startTime
        else getZonedDateTime()
    }.asLiveData()

    val skill = getSkillById.run(skillId).shareIn(viewModelScope, Eagerly, replay = 1)

    val stats = getStats.run(skillId)

    val statsChartData = stats.map { stats ->
        stats.withMissingStats().toEntries()
    }.asLiveData()

    val summary = skill.map { skill ->
        ProductivitySummary(skill.totalTime, skill.lastWeekTime)
    }.asLiveData()

    override val nameFlow = skill.map { it.name }

    fun addRecord(time: Duration) {
        val record = Record("", skillId, time)
        launchCoroutine { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { deleteSkill.run(skill) }
        logEvent("delete_skill")
    }

    fun toggleTimer() {
        stopwatchUtil.toggle(skillId)
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
