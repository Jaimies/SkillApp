package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.collectOnce
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
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
) : StatsViewModel(getStats, skillId) {

    val showRecordDialog = SingleLiveEvent<Any>()

    val stopwatchIsRunning = stopwatchUtil.state.map {
        it is Running && it.skillId == skillId
    }.asLiveData()

    val stopwatchStartTime = stopwatchUtil.state.map {
        if (it is Running) it.startTime
        else getZonedDateTime()
    }.asLiveData()

    val skill = getSkillById.run(skillId).shareIn(viewModelScope, Eagerly, replay = 1)

    val skillName = MutableLiveData("")

    val nameIsInput = skillName.map { it?.isBlank() == false }

    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> get() = _isEditing

    private val _onSave = SingleLiveEvent<Nothing>()
    val onSave: LiveData<Nothing> get() = _onSave

    val summary = skill.map { skill ->
        ProductivitySummary(skill.totalTime, skill.lastWeekTime)
    }.asLiveData()

    init {
        viewModelScope.launch {
            skill.collectOnce { skill -> skillName.value = skill.name }
        }
    }

    fun addRecord(time: Duration) {
        val record = Record("", skillId, time)
        launchCoroutine { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { deleteSkill.run(skill) }
    }

    fun toggleTimer() = stopwatchUtil.toggle(skillId)
    fun showRecordDialog() = showRecordDialog.call()

    fun enterEditingMode() {
        _isEditing.value = true
    }

    fun exitEditingMode() {
        _isEditing.value = false
    }

    fun save() {
        viewModelScope.launch {
            val name = skillName.value?.trim().orEmpty()
            updateSkillUseCase.updateName(skillId, name)
        }


        _isEditing.value = false
        _onSave.call()
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
