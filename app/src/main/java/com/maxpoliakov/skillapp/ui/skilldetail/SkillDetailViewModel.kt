package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.time.getAvgWeekHours
import com.maxpoliakov.skillapp.util.time.toReadableHours
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
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

    val skill = getSkillById.run(skillId)
        .map { it.mapToPresentation() }
        .asLiveData()

    val summary = skill.map { skill ->
        fun SkillItem.getAvgWeekTime(): String {
            val recordedTime = totalTime - initialTime
            return getAvgWeekHours(recordedTime, creationDate)
        }

        ProductivitySummary(
            skill.totalTime.toReadableHours(),
            skill.getAvgWeekTime(),
            skill.lastWeekTime.toReadableHours()
        )
    }

    fun addRecord(record: Record) {
        launchCoroutine { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.value?.let { skill ->
        ioScope.launch { deleteSkill.run(skill.mapToDomain()) }
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
