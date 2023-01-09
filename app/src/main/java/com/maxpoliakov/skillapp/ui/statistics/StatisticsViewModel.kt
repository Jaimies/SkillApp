package com.maxpoliakov.skillapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit.Millis
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.util.charts.ChartDataImpl
import com.maxpoliakov.skillapp.util.charts.PieData
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toEntries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    getStats: GetStatsUseCase,
    chartDataFactory: ChartDataImpl.Factory,
    pieDataFactory: PieData.Factory,
) : ViewModel() {

    private val criteria = SkillSelectionCriteria.WithUnit(Millis)

    // todo too long
    val chartData = chartDataFactory.create(flowOf(criteria), flowOf(null), flowOf(Millis), flowOf(null))

    val summary = getSkills.getSkills(criteria)
        .combine(getStats.getLast7DayCount(criteria), ::calculateSummary)
        .asLiveData()

    val pieData = pieDataFactory.create(getSkills.getTopSkills(5).map { it.toEntries() })
}

fun calculateSummary(skills: List<Skill>, lastWeekCount: Long): ProductivitySummary {
    return ProductivitySummary(
        totalCount = skills.sumByLong(Skill::totalCount),
        lastWeekCount = lastWeekCount,
        unit = UiMeasurementUnit.Millis
    )
}
