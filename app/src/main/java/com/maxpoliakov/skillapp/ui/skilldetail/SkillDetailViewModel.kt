package com.maxpoliakov.skillapp.ui.skilldetail

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.ui.skills.SkillState
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.time.getAvgWeekHours
import com.maxpoliakov.skillapp.util.time.getDaysSpentSince
import com.maxpoliakov.skillapp.util.time.getFriendlyHours
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

class SkillDetailViewModel(
    private val addRecord: AddRecordUseCase,
    getSkillById: GetSkillByIdUseCase,
    getStats: GetStatsUseCase,
    skillId: Int
) : StatsViewModel(getStats, skillId) {

    val showRecordDialog = SingleLiveEvent<Any>()

    val skill = getSkillById.run(skillId)
        .map { it.mapToPresentation() }
        .asLiveData()

    val state = skill.map { SkillDetailState(it) }

    fun addRecord(skillId: Int, skillName: String, time: Duration) {
        launchCoroutine {
            val record = Record(skillName, skillId, time)
            addRecord.run(record)
        }
    }

    fun showRecordDialog() = showRecordDialog.call()

    class SkillDetailState(skill: SkillItem) : SkillState(skill) {
        val avgWeekTime = getAvgWeekHours(skill.totalTime, skill.creationDate)
        val lastWeekTime = getFriendlyHours(skill.lastWeekTime)
        val daysSpent = skill.creationDate.getDaysSpentSince().toString()
    }

    class Factory @Inject constructor(
        private val addRecord: AddRecordUseCase,
        private val getSkillById: GetSkillByIdUseCase,
        private val getStats: GetStatsUseCase
    ) {
        fun create(skillId: Int): SkillDetailViewModel {
            return SkillDetailViewModel(
                addRecord,
                getSkillById,
                getStats,
                skillId
            )
        }
    }
}
