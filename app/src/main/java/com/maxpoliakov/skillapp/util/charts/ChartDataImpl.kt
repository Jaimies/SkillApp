package com.maxpoliakov.skillapp.util.charts

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ChartDataImpl @AssistedInject constructor(
    private val getStats: GetStatsUseCase,
    private val skillRepository: SkillRepository,
    private val scope: CoroutineScope,
    @Assisted
    private val criteria: SkillSelectionCriteria,
    @Assisted
    private val unit: Flow<MeasurementUnit>,
    @Assisted
    private val goal: Flow<Goal?>,
) : ChartData {
    private val statisticType = MutableStateFlow(StatisticInterval.Daily)

    val coolMutableDate = MutableStateFlow<ClosedRange<LocalDate>?>(null)

    private val statsTypes by lazy {
        StatisticInterval.values().associateBy({ it }, ::getChartData)
    }

    // todo goofy name
    override val stats = combine(statisticType, unit, goal, coolMutableDate) { interval, unit, goal, dates ->
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
        return combine(
            skillRepository.getSkills(criteria),
            coolMutableDate,
        ) { skills, dates ->
            println(dates)
            getStats.getStats(
                skills.map(Skill::id),
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
            criteria: SkillSelectionCriteria,
            unit: Flow<MeasurementUnit>,
            goal: Flow<Goal?>,
        ): ChartDataImpl
    }
}
