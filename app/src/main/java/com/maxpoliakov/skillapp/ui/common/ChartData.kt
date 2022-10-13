package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

abstract class ChartData(private val getStats: GetStatsUseCase) {
    private val statisticType = MutableStateFlow(StatisticInterval.Daily)

    private val statsTypes by lazy {
        StatisticInterval.values().associateBy({ it }, ::getChartData)
    }

    val stats = statisticType.flatMapLatest { interval ->
        statsTypes[interval]!!.map { stats ->
            BarChartData.from(interval, stats)
        }
    }.asLiveData()

    abstract val skillIdsFlow: Flow<List<Int>>

    fun setStatisticType(type: StatisticInterval) {
        statisticType.value = type
    }

    fun setStatisticType(type: UiStatisticInterval) = setStatisticType(type.toDomain())

    fun getChartData(interval: StatisticInterval): Flow<List<Statistic>> {
        return skillIdsFlow.flatMapLatest { ids ->
            getStats.getStats(ids, interval)
        }
    }
}

class SkillChartDataThatOnlyDisplaysHours(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    getStats: GetStatsUseCase,
) : ChartData(getStats) {
    override val skillIdsFlow = getSkills
        .getSkillsWithLastWeekTime(MeasurementUnit.Millis)
        .mapList<Skill, Int> { skill -> skill.id }
}

class SkillChartData(getStats: GetStatsUseCase, skillIds: List<Int>) : ChartData(getStats) {
    constructor(getStats: GetStatsUseCase, skillId: Int) : this(getStats, listOf(skillId))

    override val skillIdsFlow = flowOf(skillIds)
}

class GroupChartData(getStats: GetStatsUseCase, getGroup: GetGroupUseCase, groupId: Int) : ChartData(getStats) {
    override val skillIdsFlow = getGroup
        .getById(groupId)
        .map { group -> group.skills.map(Skill::id) }
}
