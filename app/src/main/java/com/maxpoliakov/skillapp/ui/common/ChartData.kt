package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.collectOnce
import com.maxpoliakov.skillapp.shared.util.weeksSinceEpoch
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class ChartData {
    abstract val dailyStats: LiveData<List<BarEntry>?>
    abstract val weeklyStats: LiveData<List<BarEntry>?>
    abstract val monthlyStats: LiveData<List<BarEntry>?>

    private val _statisticType = MutableLiveData(ChronoUnit.DAYS)
    val statisticType: LiveData<ChronoUnit> get() = _statisticType

    fun setStatisticType(type: ChronoUnit) {
        _statisticType.value = type
    }
}

class SkillChartDataThatOnlyDisplaysHours(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    getStats: GetStatsUseCase,
    scope: CoroutineScope
) : ChartData() {
    override val dailyStats = MediatorLiveData<List<BarEntry>?>()
    override val weeklyStats = MediatorLiveData<List<BarEntry>?>()
    override val monthlyStats = MediatorLiveData<List<BarEntry>?>()

    init {
        scope.launch {
            getSkills.getSkillsWithLastWeekTime(MeasurementUnit.Millis).collectOnce { skills ->
                val chartData = SkillChartData(getStats, skills.map(Skill::id))

                dailyStats.addSource(chartData.dailyStats) { dailyStats.value = it }
                weeklyStats.addSource(chartData.weeklyStats) { weeklyStats.value = it }
                monthlyStats.addSource(chartData.monthlyStats) { monthlyStats.value = it }
            }
        }
    }
}

class SkillChartData(getStats: GetStatsUseCase, skillIds: List<Int>) : ChartData() {
    constructor(getStats: GetStatsUseCase, skillId: Int) : this(getStats, listOf(skillId))

    override val dailyStats = getStats.getDailyStats(skillIds)
        .map { stats -> stats.withMissingDailyStats() }.asLiveData()

    override val weeklyStats = getStats.getWeeklyStats(skillIds)
        .map { stats -> stats.withMissingWeeklyStats() }.asLiveData()

    override val monthlyStats = getStats.getMonthlyStats(skillIds)
        .map { stats -> stats.withMissingMonthlyStats() }.asLiveData()
}

class GroupChartData(getStats: GetStatsUseCase, getGroup: GetGroupUseCase, groupId: Int) : ChartData() {
    private val _group = getGroup.getById(groupId)

    override val dailyStats = _group
        .flatMapLatest { group -> getStats.getDailyStats(group.skills.map(Skill::id)) }
        .map { stats -> stats.withMissingDailyStats() }.asLiveData()


    override val weeklyStats = _group
        .flatMapLatest { group -> getStats.getWeeklyStats(group.skills.map(Skill::id)) }
        .map { stats -> stats.withMissingWeeklyStats() }.asLiveData()

    override val monthlyStats = _group
        .flatMapLatest { group -> getStats.getMonthlyStats(group.skills.map(Skill::id)) }
        .map { stats -> stats.withMissingMonthlyStats() }.asLiveData()
}

private fun List<Statistic>.withMissingDailyStats() =
    withMissingStats(ChronoUnit.DAYS, LocalDate.now(), 56).toEntries(ChronoUnit.DAYS)

private fun List<Statistic>.withMissingWeeklyStats() =
    withMissingStats(ChronoUnit.WEEKS, LocalDate.now().atStartOfWeek(), 21)
        .toEntries(ChronoUnit.WEEKS, LocalDate::weeksSinceEpoch)

private fun List<Statistic>.withMissingMonthlyStats() =
    withMissingStats(ChronoUnit.MONTHS, LocalDate.now().withDayOfMonth(1), 21).toEntries(ChronoUnit.MONTHS)
