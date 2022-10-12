package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.sumByLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val statsRepository: SkillStatsRepository
) {
    fun getStats(skillIds: List<Int>, interval: StatisticInterval) : Flow<List<Statistic>> {
        return combine(skillIds.map { id -> getStats(id, interval) }) { stats ->
            group(stats, interval)
        }
    }

    fun getStats(skillId: Int, interval: StatisticInterval): Flow<List<Statistic>> {
        val startDate = LocalDate.now()
            .minus(interval.numberOfValues.toLong(), interval.unit)

        val dailyStats = statsRepository.getStats(skillId, startDate)

        return dailyStats.map { stats ->
            stats
                .groupBy { interval.toNumber(it.date) }
                .map { entry ->
                    Statistic(
                        interval.atStartOfInterval(entry.value[0].date),
                        entry.value.sumByLong(Statistic::count)
                    )
                }
        }
    }

    private fun group(statsLists: Array<List<Statistic>>, interval: StatisticInterval) = statsLists
        .flatMap { it }
        .groupBy { interval.toNumber(it.date) }
        .map { entry ->
            Statistic(entry.value[0].date, entry.value.sumByLong(Statistic::count))
        }
}
