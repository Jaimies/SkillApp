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
        interval: StatisticInterval,
        dates: ClosedRange<LocalDate>,
    ): Flow<List<Statistic>>
}
