package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetStatsUseCase {
    fun getStats(criteria: SkillSelectionCriteria, dates: ClosedRange<LocalDate>): Flow<List<Statistic>>

    fun getGroupedStats(
        criteria: SkillSelectionCriteria,
        dates: ClosedRange<LocalDate>,
        interval: StatisticInterval
    ): Flow<List<Statistic>>

    fun getLast7DayCount(criteria: SkillSelectionCriteria): Flow<Long>
}
