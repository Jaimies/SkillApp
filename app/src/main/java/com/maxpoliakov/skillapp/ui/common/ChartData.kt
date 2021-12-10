package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.weeksSinceEpoch
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

class SkillChartData(getStats: GetStatsUseCase, skillId: Int) : ChartData() {
    override val dailyStats = getStats.getDailyStats(skillId)
        .map { stats -> stats.withMissingDailyStats() }.asLiveData()

    override val weeklyStats = getStats.getWeeklyStats(skillId)
        .map { stats -> stats.withMissingWeeklyStats() }.asLiveData()

    override val monthlyStats = getStats.getMonthlyStats(skillId)
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
    withMissingStats(ChronoUnit.DAYS, LocalDate.now()).toEntries(ChronoUnit.DAYS)

private fun List<Statistic>.withMissingWeeklyStats() =
    withMissingStats(ChronoUnit.WEEKS, LocalDate.now().atStartOfWeek())
        .toEntries(ChronoUnit.WEEKS, LocalDate::weeksSinceEpoch)

private fun List<Statistic>.withMissingMonthlyStats() =
    withMissingStats(ChronoUnit.MONTHS, LocalDate.now().withDayOfMonth(1)).toEntries(ChronoUnit.MONTHS)
