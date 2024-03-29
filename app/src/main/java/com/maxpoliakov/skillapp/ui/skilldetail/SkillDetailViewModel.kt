package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
import com.maxpoliakov.skillapp.domain.time.DateProvider
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.DetailsViewModel
import com.maxpoliakov.skillapp.shared.analytics.logEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
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
    private val dateProvider: DateProvider,
    args: SkillDetailFragmentArgs,
    getSkillById: GetSkillByIdUseCase,
    recordsRepository: RecordsRepository,
) : DetailsViewModel(
    stopwatch,
    getSkillById.run(args.skillId),
) {
    private val skillId = args.skillId
    override val selectionCriteria = SkillSelectionCriteria.WithId(skillId)

    private val _stopwatchStarted = SingleLiveEventWithoutData()
    val stopwatchStarted: LiveData<Unit> get() = _stopwatchStarted

    val showRecordDialog = SingleLiveEventWithoutData()

    private val _showRecordAdded = SingleLiveEvent<List<Record>>()
    val showRecordAdded: LiveData<List<Record>> get() = _showRecordAdded

    val latestRecord = recordsRepository
        .getLatestRecordForSkillWithId(skillId)
        .stateIn(viewModelScope, Eagerly, null)

    val stopwatchIsRunning = stopwatch.state.map {
        it.hasTimerForSkillId(skillId)
    }.asLiveData()

    val stopwatchStartTime = stopwatch.state.map {
        it.getTimerForSkillId(skillId)?.startTime ?: getZonedDateTime()
    }.asLiveData()

    val skill = getSkillById.run(skillId)
    private val skillStateFlow = skill.stateIn(viewModelScope, Eagerly, null)

    val summary by lazy {
        skill.combine(lastWeekTime, ProductivitySummary.Companion::from).asLiveData()
    }

    override fun getApplicableTimers(state: Stopwatch.State): List<Timer> {
        return listOfNotNull(state.getTimerForSkillId(skillId))
    }

    fun addRecord(count: Long) = skillStateFlow.value?.let { skill ->
        val record = Record(
            name = "",
            skillId = skillId,
            count = count,
            unit = skill.unit,
            date = dateProvider.getCurrentDateWithRespectToDayStartTime(),
        )

        scope.launch { addRecord.run(record) }
    }

    fun deleteSkill() = skillStateFlow.value?.let { skill ->
        scope.launch { manageSkill.deleteSkill(skill) }
        logEvent("delete_skill")
    }

    fun toggleTimer() = scope.launch {
        val stateChange = stopwatch.toggle(skillId)
        _showRecordAdded.value = stateChange.addedRecords
        if (stateChange is StateChange.Start) {
            _stopwatchStarted.call()
        }
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
