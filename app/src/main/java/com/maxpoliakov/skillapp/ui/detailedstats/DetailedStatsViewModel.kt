package com.maxpoliakov.skillapp.ui.detailedstats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit.Millis
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria.WithId
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria.WithIdInList
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria.WithUnit
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.shared.util.FullLocalDateRange
import com.maxpoliakov.skillapp.util.charts.ChartDataImpl
import com.maxpoliakov.skillapp.util.charts.PieData
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toEntries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailedStatsViewModel @Inject constructor(
    private val skillRepository: SkillRepository,
    private val getHistory: GetHistoryUseCase,
    chartDataFactory: ChartDataImpl.Factory,
    pieDataFactory: PieData.Factory,
) : ViewModel() {
    val startDate = MutableLiveData("2022-11-12")
    val endDate = MutableLiveData("2022-11-13")
    val skillIds = MutableLiveData("1,2,3")

    val allSkills = skillRepository.getSkills()
        .map {
            it
                .sortedBy { skill -> skill.id }
                .joinToString("\n") { skill -> "${skill.id} => ${skill.name}" }
        }
        .asLiveData()

    private val criteria = MutableStateFlow<SkillSelectionCriteria>(
        SkillSelectionCriteria.WithUnit(Millis)
    )

    private val dateRange = MutableStateFlow<ClosedRange<LocalDate>?>(null)

    private val theSkillIds = MutableStateFlow<List<Id>>(listOf())

    private val entries = combine(theSkillIds, dateRange) { skillIds, dateRange ->
        skillRepository
            .getSkills(WithIdInList(skillIds))
            .map { skills -> skills.toPieEntries(dateRange) }
    }
        .flatMapLatest { it }

    val chartData = chartDataFactory.create(
        criteria,
        dateRange,
        flowOf(Millis),
        flowOf(null),
    )

    val pieData = pieDataFactory.create(entries)

    fun showStats(): Boolean {
        val startDate = LocalDate.parse(this.startDate.value)
        val endDate = LocalDate.parse(this.endDate.value)
        val skillIds = this.skillIds.value!!
            .split(",")
            .mapNotNull(String::toIntOrNull)

        this.theSkillIds.value = skillIds

        dateRange.value = startDate..endDate
        criteria.value = getCriteria(skillIds)

        return false
    }

    private fun getCriteria(skillIds: List<Int>): SkillSelectionCriteria {
        if (skillIds.isEmpty()) {
            return WithUnit(Millis)
        }

        return WithIdInList(skillIds).withUnit(Millis)
    }

    private suspend fun List<Skill>.toPieEntries(dateRange: ClosedRange<LocalDate>?) = toEntries { skill ->
        getHistory.getCount(WithId(skill.id), dateRange ?: FullLocalDateRange).first()
    }
}
