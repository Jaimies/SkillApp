package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.domain.time.DateProvider
import kotlinx.coroutines.flow.Flow

open class GetRecentCountUseCaseImpl(
    private val statsRepository: StatsRepository,
    private val dateProvider: DateProvider,
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(skillId: Int, interval: StatisticInterval): Flow<Long> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()
        val startOfInterval = interval.atStartOfInterval(currentDate)
        return statsRepository.getCount(skillId, startOfInterval..currentDate)
    }
}
