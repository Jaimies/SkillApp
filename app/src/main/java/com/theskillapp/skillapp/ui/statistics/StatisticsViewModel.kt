package com.theskillapp.skillapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.domain.model.MeasurementUnit.Millis
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.theskillapp.skillapp.domain.usecase.stats.GetRecentCountUseCase
import com.theskillapp.skillapp.model.ProductivitySummary
import com.theskillapp.skillapp.model.UiMeasurementUnit
import com.theskillapp.skillapp.shared.chart.ChartDataImpl
import com.theskillapp.skillapp.shared.util.sumByLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    getRecentCountUseCase: GetRecentCountUseCase,
    chartDataFactory: ChartDataImpl.Factory,
) : ViewModel() {

    private val criteria = SkillSelectionCriteria.WithUnit(Millis)

    val chartData = chartDataFactory.create(flowOf(criteria), flowOf(Millis), flowOf(null))

    val summary = getSkills.getSkills(criteria)
        .combine(getRecentCountUseCase.getLast7DayCount(criteria), ::calculateSummary)
        .stateIn(viewModelScope, SharingStarted.Eagerly, ProductivitySummary.ZERO)
}

fun calculateSummary(skills: List<Skill>, lastWeekCount: Long): ProductivitySummary {
    return ProductivitySummary(
        totalCount = skills.sumByLong(Skill::totalCount),
        lastWeekCount = lastWeekCount,
        unit = UiMeasurementUnit.Millis
    )
}
