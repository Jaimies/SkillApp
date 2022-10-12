package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.temporal.ChronoUnit

open class GetRecentCountUseCaseImpl(
    private val statsRepository: StatsRepository
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(skillId: Int, interval: StatisticInterval): Flow<Long> {
        val startOfInterval = interval.atStartOfInterval(getCurrentDate())
        val daysCount = startOfInterval.until(getCurrentDate(), ChronoUnit.DAYS) + 1
        val dailyTimes = List(daysCount.toInt()) { index ->
            statsRepository.getCountAtDate(skillId, startOfInterval.plusDays(index.toLong()))
        }

        return combine(dailyTimes) { times -> times.sum() }
    }
}
