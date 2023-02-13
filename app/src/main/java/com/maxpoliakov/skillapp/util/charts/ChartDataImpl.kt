package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria.*
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toPieEntries
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toEntries
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria as Criteria

class ChartDataImpl @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    private val skillRepository: SkillRepository,
    private val getSkillsAndSkillGroups: GetSkillsAndSkillGroupsUseCase,
    @ApplicationContext
    private val context: Context,
    @Assisted
    private val criteria: Flow<Criteria>,
    @Assisted
    private val dateRange: Flow<ClosedRange<LocalDate>?>,
    @Assisted
    private val unit: Flow<MeasurementUnit<*>>,
    @Assisted
    private val goal: Flow<Goal?>,
) : ChartData {
    private val statisticType = MutableStateFlow(StatisticInterval.Daily)

    private val statsTypes by lazy {
        StatisticInterval.values().associateBy({ it }, ::getChartData)
    }

    private val selectedEntry = MutableStateFlow<Entry?>(null)

    private val _selectedDateRange = selectedEntry.combine(statisticType) { entry, type ->
        entry?.x?.toLong()?.let { xValue ->
            type.getIntervalContaining(type.toDate(xValue))
        }
    }

    override val selectedDateRange = _selectedDateRange.asLiveData()

    private val state = combine(criteria, dateRange, unit, statisticType, goal, ::State)

    override val pieData = state.combine(_selectedDateRange, this::getPieEntries)
        .flatMapLatest { it }
        .map { skillPieEntries ->
            toPieEntries(skillPieEntries)?.let(::PieChartData)
        }
        .asLiveData()

    // todo goofy name
    override val stats = state.flatMapLatest { state ->
        statsTypes[state.interval]!!.map { stats ->
            makeBarChartData(state, stats)
        }
    }.asLiveData()

    private fun makeBarChartData(state: State, stats: List<Statistic>): BarChartData? {
        return BarChartData.from(
            state.interval, stats, state.unit, state.goal,
            state.dateRange ?: LocalDate.now()
                .minus(state.interval.numberOfValues.toLong() - 1, state.interval.unit)
                .rangeTo(LocalDate.now()),
        )
    }

    override fun setSelectedEntry(entry: Entry?) {
        selectedEntry.value = entry
    }

    override fun setStatisticType(type: StatisticInterval) {
        statisticType.value = type
        selectedEntry.value = null
    }

    override fun setStatisticType(type: UiStatisticInterval) = setStatisticType(type.toDomain())

    override fun getChartData(interval: StatisticInterval): Flow<List<Statistic>> {
        return state.map { state ->
            getStats.getGroupedStats(
                state.criteria,
                state.dateRange ?: LocalDate.now()
                    .minus(interval.numberOfValues.toLong() - 1, interval.unit)
                    .rangeTo(LocalDate.now()),
                interval,
            )
        }.flatMapLatest { it }
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
            skills
                .sortedByDescending(Skill::totalCount)
                .take(5)
                .toEntries()
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
                SkillPieEntry(skill, stats.sumByLong(Statistic::count))
            }
    }

    private fun toPieEntries(entries: List<SkillPieEntry>): List<ThePieEntry>? {
        return entries
            .takeIf(List<SkillPieEntry>::isNotEmpty)
            ?.filter(SkillPieEntry::hasPositiveValue)
            ?.sortedByDescending(SkillPieEntry::count)
            ?.take(5)
            ?.toPieEntries(context)
    }

    data class State(
        val criteria: Criteria,
        val dateRange: ClosedRange<LocalDate>?,
        val unit: MeasurementUnit<*>,
        val interval: StatisticInterval,
        val goal: Goal?,
    )

    @AssistedFactory
    interface Factory {
        fun create(
            criteria: Flow<Criteria>,
            dateRange: Flow<ClosedRange<LocalDate>?>,
            unit: Flow<MeasurementUnit<*>>,
            goal: Flow<Goal?>,
        ): ChartDataImpl
    }
}
