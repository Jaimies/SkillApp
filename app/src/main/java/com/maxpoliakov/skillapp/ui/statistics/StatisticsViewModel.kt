package com.maxpoliakov.skillapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.ui.common.SkillChartDataThatOnlyDisplaysHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    getStats: GetStatsUseCase
) : ViewModel() {
    val chartData = SkillChartDataThatOnlyDisplaysHours(getSkills, getStats, viewModelScope)

    val summary = getSkills.getSkillsWithLastWeekTime(MeasurementUnit.Millis).map { skills ->
        if (skills.isEmpty())
            return@map ProductivitySummary.ZERO

        calculateSummary(skills)
    }.asLiveData()

    val pieData = getSkills.getTopSkills(5).mapList { skill ->
        PieEntry(skill.totalCount.toFloat(), skill.name)
    }.asLiveData()
}

fun calculateSummary(skills: List<Skill>): ProductivitySummary {
    return ProductivitySummary(
        totalCount = skills.sumByLong { it.totalCount },
        lastWeekCount = skills.sumByLong { it.lastWeekCount },
        unit = UiMeasurementUnit.Millis
    )
}
