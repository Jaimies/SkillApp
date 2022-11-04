package com.maxpoliakov.skillapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit.Millis
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria.WithUnit
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.ui.common.ChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    chartDataFactory: ChartData.Factory,
) : ViewModel() {
    val chartData = chartDataFactory.create(WithUnit(Millis), flowOf(Millis), flowOf(null))

    val summary = getSkills.getSkillsWithLastWeekTime(MeasurementUnit.Millis).map { skills ->
        calculateSummary(skills)
    }.asLiveData()

    val pieData = getSkills.getTopSkills(5).mapList { skill ->
        PieEntry(skill.totalCount.toFloat(), skill.name)
    }.asLiveData()
}

fun calculateSummary(skills: List<Skill>): ProductivitySummary {
    return ProductivitySummary(
        totalCount = skills.sumByLong(Skill::totalCount),
        lastWeekCount = skills.sumByLong(Skill::lastWeekCount),
        unit = UiMeasurementUnit.Millis
    )
}
