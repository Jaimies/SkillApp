package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentSkillCountUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.SkillChartData
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillDetailViewModel @Inject constructor(
    private val addRecord: AddRecordUseCase,
    private val manageSkill: ManageSkillUseCase,
    private val ioScope: CoroutineScope,
    private val stopwatchUtil: StopwatchUtil,
    args: SkillDetailFragmentArgs,
    getSkillById: GetSkillByIdUseCase,
    getRecentCount: GetRecentSkillCountUseCase,
    getStats: GetStatsUseCase,
) : DetailsViewModel(
    stopwatchUtil,
    getRecentCount,
    getSkillById.run(args.skillId),
) {
    private val skillId = args.skillId
    override val selectionCriteria = SkillSelectionCriteria.WithId(skillId)

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

    val uiGoal = skillLiveData.map { skill -> skill.goal?.mapToUI(skill.unit) }
    override val unitFlow = skill.map { skill -> skill.unit }

    val summary = skill.map { skill ->
        ProductivitySummary.from(skill)
    }.asLiveData()

    val chartData = SkillChartData(getStats, skillId)

    override val nameFlow = skill.map { it.name }

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.skillId == skillId
    }

    fun addRecord(count: Long) {
        val record = Record("", skillId, count, skillLiveData.value!!.unit)
        ioScope.launch { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { manageSkill.deleteSkill(skill) }
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
        manageSkill.updateSkill(skillId, name, goal.value?.mapToDomain())
    }
}
