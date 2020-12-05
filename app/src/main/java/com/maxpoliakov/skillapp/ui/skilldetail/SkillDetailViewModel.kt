package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.ui.skills.SkillState
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.time.getAvgWeekHours
import com.maxpoliakov.skillapp.util.time.getFriendlyHours
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

    val state = skill.map { SkillDetailState(it) }

    fun addRecord(record: Record) {
        launchCoroutine { addRecord.run(record) }
    }

    fun deleteSkill() = this.skill.value?.let { skill ->
        ioScope.launch { deleteSkill.run(skill.mapToDomain()) }
    }

    fun showRecordDialog() = showRecordDialog.call()

    class SkillDetailState(skill: SkillItem) : SkillState(skill) {
        val avgWeekTime = getAvgWeekTime(skill)
        val lastWeekTime = getFriendlyHours(skill.lastWeekTime)

        private fun getAvgWeekTime(skill: SkillItem): String {
            val recordedTime = skill.totalTime - skill.initialTime
            return getAvgWeekHours(recordedTime, skill.creationDate)
        }
    }

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
