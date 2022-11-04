package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ChartData @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    private val skillRepository: SkillRepository,
    @Assisted
    private val criteria: SkillSelectionCriteria,
    @Assisted
    private val unit: Flow<MeasurementUnit>,
    @Assisted
    private val goal: Flow<Goal?>,
) {
    private val statisticType = MutableStateFlow(StatisticInterval.Daily)

    private val statsTypes by lazy {
        StatisticInterval.values().associateBy({ it }, ::getChartData)
    }

    val stats = statisticType.flatMapLatest { interval ->
        statsTypes[interval]!!.map { stats ->
            BarChartData.from(interval, stats, unit.first(), goal.first())
        }
    }.asLiveData()

    fun setStatisticType(type: StatisticInterval) {
        statisticType.value = type }

    fun setStatisticType(type: UiStatisticInterval) = setStatisticType(type.toDomain())

    fun getChartData(interval: StatisticInterval): Flow<List<Statistic>> {
        return skillRepository.getSkills(criteria).flatMapLatest { skills ->
            getStats.getStats(skills.map(Skill::id), interval)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(criteria: SkillSelectionCriteria, unit: Flow<MeasurementUnit>, goal: Flow<Goal?>): ChartData
    }
}
