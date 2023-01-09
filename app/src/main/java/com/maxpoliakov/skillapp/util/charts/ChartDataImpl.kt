package com.maxpoliakov.skillapp.util.charts

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ChartDataImpl @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    @Assisted
    private val criteria: Flow<SkillSelectionCriteria>,
    @Assisted
    private val dateRange: Flow<ClosedRange<LocalDate>?>,
    @Assisted
    private val unit: Flow<MeasurementUnit>,
    @Assisted
    private val goal: Flow<Goal?>,
) : ChartData {
    private val statisticType = MutableStateFlow(StatisticInterval.Daily)

    private val statsTypes by lazy {
        StatisticInterval.values().associateBy({ it }, ::getChartData)
    }

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

    override fun setStatisticType(type: StatisticInterval) {
        statisticType.value = type
    }

    override fun setStatisticType(type: UiStatisticInterval) = setStatisticType(type.toDomain())

    override fun getChartData(interval: StatisticInterval): Flow<List<Statistic>> {
        return combine(criteria, dateRange) { criteria, dates ->
            getStats.getStats(
                criteria,
                dates ?: LocalDate.now()
                    .minus(interval.numberOfValues.toLong() - 1, interval.unit)
                    .rangeTo(LocalDate.now()),
                interval,
            )
        }.flatMapLatest { it }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            criteria: Flow<SkillSelectionCriteria>,
            dateRange: Flow<ClosedRange<LocalDate>?>,
            unit: Flow<MeasurementUnit>,
            goal: Flow<Goal?>,
        ): ChartDataImpl
    }
}
