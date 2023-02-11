package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.coroutines.flow.Flow

open class GetRecentCountUseCaseImpl(
    private val statsRepository: StatsRepository,
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(skillId: Int, interval: StatisticInterval): Flow<Long> {
        val startOfInterval = interval.atStartOfInterval(getCurrentDate())
        return statsRepository.getCount(skillId, startOfInterval..getCurrentDate())
    }
}
