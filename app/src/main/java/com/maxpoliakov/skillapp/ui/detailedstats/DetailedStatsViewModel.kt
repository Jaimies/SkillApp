package com.maxpoliakov.skillapp.ui.detailedstats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit.Millis
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.util.charts.ChartDataImpl
import com.maxpoliakov.skillapp.util.charts.PieData
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toEntries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailedStatsViewModel @Inject constructor(
    private val skillRepository: SkillRepository,
    private val getHistory: GetHistoryUseCase,
    chartDataFactory: ChartDataImpl.Factory,
    pieDataFactory: PieData.Factory,
) : ViewModel() {
    val startDate = MutableLiveData("")
    val endDate = MutableLiveData("")
    val skillIds = MutableLiveData("")

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

    val chartData = chartDataFactory.create(
        criteria,
        flowOf(Millis),
        flowOf(null),
    )

    val skills = MutableStateFlow<List<SkillPieEntry>>(listOf())
    val pieData = pieDataFactory.create(skills)

    fun showStats(): Boolean {
        val startDate = LocalDate.parse(this.startDate.value)
        val endDate = LocalDate.parse(this.endDate.value)
        val skillIds = this.skillIds.value!!
            .split(",")
            .mapNotNull(String::toIntOrNull)

        chartData.coolMutableDate.value = startDate..endDate
        criteria.value = getCriteria(skillIds)

        viewModelScope.launch {
            skills.value = skillRepository.getSkills(SkillSelectionCriteria.WithIdInList(skillIds)).map { skills ->
                skills.toEntries { skill ->
                    getHistory.getCount(
                        SkillSelectionCriteria.WithId(skill.id),
                        chartData.coolMutableDate.value!!,
                    )
                }
            }.first()
        }

        return false
    }

    private fun getCriteria(skillIds: List<Int>): SkillSelectionCriteria {
        if (skillIds.isEmpty()) {
            return SkillSelectionCriteria.WithUnit(Millis)
        }

        return SkillSelectionCriteria
            .WithIdInList(skillIds)
            .withUnit(Millis)
    }
}
