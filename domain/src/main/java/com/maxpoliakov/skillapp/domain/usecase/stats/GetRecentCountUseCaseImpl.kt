package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import java.time.Clock
import java.time.LocalDate

open class GetRecentCountUseCaseImpl(
    private val statsRepository: StatsRepository,
    private val clock: Clock,
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(skillId: Int, interval: StatisticInterval): Flow<Long> {
        val startOfInterval = interval.atStartOfInterval(LocalDate.now(clock))
        return statsRepository.getCount(skillId, startOfInterval..LocalDate.now(clock))
    }
}
