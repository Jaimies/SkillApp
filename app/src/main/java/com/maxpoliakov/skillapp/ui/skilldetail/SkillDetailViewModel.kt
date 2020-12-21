package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.statistics.getTodayTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

class SkillDetailViewModel(
    private val addRecord: AddRecordUseCase,
    private val deleteSkill: DeleteSkillUseCase,
    private val ioScope: CoroutineScope,
    getSkillById: GetSkillByIdUseCase,
    getStats: GetStatsUseCase,
    skillId: Int
) : StatsViewModel(getStats, skillId) {

    val showRecordDialog = SingleLiveEvent<Any>()

    val skill = getSkillById.run(skillId).shareIn(viewModelScope, Eagerly, replay = 1)

    val summary = skill.zip(stats) { skill, stats ->
        ProductivitySummary(
            skill.totalTime,
            stats.getTodayTime(),
            skill.lastWeekTime
        )
    }.asLiveData()

    fun addRecord(record: Record) {
        launchCoroutine { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.replayCache.lastOrNull()?.let { skill ->
        ioScope.launch { deleteSkill.run(skill) }
    }

    fun showRecordDialog() = showRecordDialog.call()

    class Factory @Inject constructor(
        private val addRecord: AddRecordUseCase,
        private val getSkillById: GetSkillByIdUseCase,
        private val getStats: GetStatsUseCase,
        private val deleteSkill: DeleteSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(skillId: Int): SkillDetailViewModel {
            return SkillDetailViewModel(
                addRecord,
                deleteSkill,
                ioScope,
                getSkillById,
                getStats,
                skillId
            )
        }
    }
}
