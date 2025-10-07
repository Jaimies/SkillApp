package com.theskillapp.skillapp.shared.chart

import android.content.Context
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.highlight.Highlight
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria.*
import com.theskillapp.skillapp.domain.model.Statistic
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.time.DateProvider
import com.theskillapp.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.theskillapp.skillapp.domain.usecase.stats.GetStatsUseCase
import com.theskillapp.skillapp.model.BarChartData
import com.theskillapp.skillapp.model.PieChartData
import com.theskillapp.skillapp.model.UiStatisticInterval
import com.theskillapp.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.theskillapp.skillapp.shared.util.sumByLong
import com.theskillapp.skillapp.shared.chart.SkillPieEntry.Companion.toSkillPieEntries
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria as Criteria

class ChartDataImpl @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    private val skillRepository: SkillRepository,
    private val getSkillsAndSkillGroups: GetSkillsAndSkillGroupsUseCase,
    private val dateProvider: DateProvider,
    @ApplicationContext
    private val context: Context,
    @Assisted
    private val criteria: Flow<Criteria>,
    @Assisted
    private val unit: Flow<MeasurementUnit<*>>,
    @Assisted
    private val goal: Flow<Goal?>,
) : ChartData {
    private val _interval = MutableStateFlow(StatisticInterval.Daily)
    override val interval = _interval.map { it.mapToUI() }.asLiveData()

    private val statisticsByInterval by lazy {
        StatisticInterval.values().associateBy({ it }, ::getStatistics)
    }

    override val barChartHighlight = MutableStateFlow<Highlight?>(null)
    override val pieChartHighlight = MutableStateFlow<Highlight?>(null)

    private val _selectedDateRange = barChartHighlight.combine(_interval) { highlight, interval ->
        highlight?.x?.toLong()?.let(interval::toDateRange)
    }

    override val selectedDateRange = _selectedDateRange.asLiveData()

    private val state = combine(criteria, unit, _interval, goal) { criteria, unit, interval, goal ->
        State(criteria, unit, interval, goal, interval.getDateRangeForStatistics())
    }

    override val pieChartData = state
        .combine(_selectedDateRange, this::getPieEntries)
        .flatMapLatest { it }
        .map { entries ->
            entries
                .organize()
                ?.let(::PieChartData)
        }
        .asLiveData()

    override val barChartData = state.flatMapLatest { state ->
        statisticsByInterval[state.interval]!!.map { stats ->
            makeBarChartData(state, stats)
        }
    }.asLiveData()

    private fun makeBarChartData(state: State, stats: List<Statistic>): BarChartData? {
        return BarChartData.from(state.interval, stats, state.unit, state.goal, state.dateRangeForStatistics)
    }

    override fun setInterval(interval: UiStatisticInterval) {
        _interval.value = interval.domainCounterpart
        barChartHighlight.value = null
        pieChartHighlight.value = null
    }

    private fun getStatistics(interval: StatisticInterval): Flow<List<Statistic>> {
        return state.map { state ->
            getStats.getGroupedStats(state.criteria, interval, state.dateRangeForStatistics)
        }.flatMapLatest { it }
    }

    private fun List<SkillPieEntry>.organize(): List<SkillPieEntry>? {
        return takeIf(List<SkillPieEntry>::isNotEmpty)
            ?.filter(SkillPieEntry::hasPositiveValue)
            ?.sortedByDescending(SkillPieEntry::count)
            ?.take(5)
    }

    private fun getPieEntries(state: State, selectedDateRange: ClosedRange<LocalDate>?): Flow<List<SkillPieEntry>> {
        return if (selectedDateRange == null) {
            getPieEntriesForTotalCount(state)
        } else {
            getPieEntries(state.criteria, selectedDateRange)
        }
    }

    private fun getPieEntriesForTotalCount(state: State): Flow<List<SkillPieEntry>> {
        return getSkillsAndSkillGroups.getSkills(state.criteria).map { skills ->
            skills.toSkillPieEntries(context)
        }
    }

    private fun getPieEntries(criteria: Criteria, dateRange: ClosedRange<LocalDate>): Flow<List<SkillPieEntry>> {
        return skillRepository.getSkills(criteria).flatMapLatest { skills ->
            getPieEntries(skills, dateRange)
        }
    }

    private fun getPieEntries(skills: List<Skill>, dateRange: ClosedRange<LocalDate>): Flow<List<SkillPieEntry>> {
        return combine(
            skills.map { skill -> getPieEntry(skill, dateRange) },
            Array<SkillPieEntry>::toList,
        )
    }

    private fun getPieEntry(skill: Skill, dateRange: ClosedRange<LocalDate>): Flow<SkillPieEntry> {
        return getStats.getStats(WithId(skill.id), dateRange)
            .map { stats ->
                SkillPieEntry.create(skill, stats.sumByLong(Statistic::count), context)
            }
    }

    private fun StatisticInterval.getDateRangeForStatistics(): ClosedRange<LocalDate> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()
        return getDateRangeOfNIntervalsUpUntilIntervalContaining(currentDate, numberOfValues.toLong())
    }

    data class State(
        val criteria: Criteria,
        val unit: MeasurementUnit<*>,
        val interval: StatisticInterval,
        val goal: Goal?,
        val dateRangeForStatistics: ClosedRange<LocalDate>,
    )

    @AssistedFactory
    interface Factory {
        fun create(
            criteria: Flow<Criteria>,
            unit: Flow<MeasurementUnit<*>>,
            goal: Flow<Goal?>,
        ): ChartDataImpl
    }
}
