package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentSkillCountUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillDetailViewModel @Inject constructor(
    private val addRecord: AddRecordUseCase,
    private val manageSkill: ManageSkillUseCase,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val stopwatch: Stopwatch,
    args: SkillDetailFragmentArgs,
    getSkillById: GetSkillByIdUseCase,
    getRecentCount: GetRecentSkillCountUseCase,
    recordsRepository: RecordsRepository,
) : DetailsViewModel(
    stopwatch,
    getRecentCount,
    getSkillById.run(args.skillId),
) {
    private val skillId = args.skillId
    override val selectionCriteria = SkillSelectionCriteria.WithId(skillId)

    val showRecordDialog = SingleLiveEvent<Any>()
    private val _showRecordAdded = SingleLiveEvent<Record>()
    val showRecordAdded: LiveData<Record?> get() = _showRecordAdded

    val latestRecord = recordsRepository
        .getLatestRecordForSkillWithId(skillId)
        .stateIn(viewModelScope, Eagerly, null)

    val stopwatchIsRunning = stopwatch.state.map {
        it is Running && it.skillId == skillId
    }.asLiveData()

    val stopwatchStartTime = stopwatch.state.map {
        if (it is Running) it.startTime
        else getZonedDateTime()
    }.asLiveData()

    val skill = getSkillById.run(skillId)
    private val skillStateFlow = skill.stateIn(viewModelScope, Eagerly, null)

    val summary by lazy {
        skill.combine(lastWeekTime, ProductivitySummary.Companion::from).asLiveData()
    }

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.skillId == skillId
    }

    fun addRecord(count: Long) = skillStateFlow.value?.let { skill ->
        val record = Record("", skillId, count, skill.unit)
        scope.launch { addRecord.run(record) }
    }

    fun deleteSkill() = skillStateFlow.value?.let { skill ->
        scope.launch { manageSkill.deleteSkill(skill) }
        logEvent("delete_skill")
    }

    fun toggleTimer() = scope.launch {
        val record = stopwatch.toggle(skillId)
        if (record != null) _showRecordAdded.value = record
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
