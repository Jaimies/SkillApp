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
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.SkillChartData
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
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

    val summary = skill.map { skill ->
        ProductivitySummary(skill.totalTime, skill.lastWeekTime)
    }.asLiveData()

    val chartData = SkillChartData(getStats, skillId)

    override val nameFlow = skill.map { it.name }

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
