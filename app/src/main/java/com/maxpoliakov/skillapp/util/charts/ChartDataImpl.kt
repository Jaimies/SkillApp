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
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.shared.util.daysSinceEpoch
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
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria as Criteria

class ChartDataImpl @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    private val skillRepository: SkillRepository,
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

    private val entry = MutableStateFlow<Entry?>(Entry(LocalDate.now().daysSinceEpoch.toFloat(), 0f))

    private val selectedDateRange = entry.combine(statisticType) { entry, type ->
        entry?.x?.toLong()?.let { num ->
            type.toDate(num)..type.toDate(num)
        }
    }

    override val pieData = selectedDateRange.combine(criteria, this::getPieEntries)
        .flatMapLatest { it }
        .map { skillPieEntries ->
            PieChartData(toPieEntries(skillPieEntries))
        }
        .asLiveData()

    // todo goofy name
    override val stats = combine(statisticType, unit, goal, dateRange) { interval, unit, goal, dates ->
        statsTypes[interval]!!.map { stats ->
            // todo maybe pass fewer arguments
            BarChartData.from(
                interval, stats, unit, goal,
                dates ?: LocalDate.now()
                    .minus(interval.numberOfValues.toLong() - 1, interval.unit)
                    .rangeTo(LocalDate.now()),
            )
        }
    }
        .flatMapLatest { it }
        .asLiveData()

    override fun setSelectedEntry(entry: Entry?) {
        if (entry == null) {
            // todo handle null as well
            return
        }
        this.entry.value = entry
    }

    override fun setStatisticType(type: StatisticInterval) {
        statisticType.value = type
    }

    override fun setStatisticType(type: UiStatisticInterval) = setStatisticType(type.toDomain())

    override fun getChartData(interval: StatisticInterval): Flow<List<Statistic>> {
        return combine(criteria, dateRange) { criteria, dates ->
            getStats.getGroupedStats(
                criteria,
                dates ?: LocalDate.now()
                    .minus(interval.numberOfValues.toLong() - 1, interval.unit)
                    .rangeTo(LocalDate.now()),
                interval,
            )
        }.flatMapLatest { it }
    }

    private fun getPieEntries(dateRange: ClosedRange<LocalDate>?, criteria: Criteria): Flow<List<SkillPieEntry>> {
        return if (dateRange == null) {
            TODO()
        } else {
            getPieEntries(criteria, dateRange)
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

    private fun toPieEntries(entries: List<SkillPieEntry>): List<ThePieEntry> {
        return entries.filter(SkillPieEntry::hasPositiveValue)
            .sortedByDescending(SkillPieEntry::count)
            .take(5)
            .toPieEntries(context)
    }

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
