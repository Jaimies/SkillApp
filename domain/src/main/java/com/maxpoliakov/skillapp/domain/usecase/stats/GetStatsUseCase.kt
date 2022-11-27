package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetStatsUseCase {
    fun getStats(
        skillIds: List<Int>,
        dates: ClosedRange<LocalDate>,
        interval: StatisticInterval
    ): Flow<List<Statistic>>

    fun getStats(
        skillId: Int,
        dates: ClosedRange<LocalDate>,
        interval: StatisticInterval
    ): Flow<List<Statistic>>
}
