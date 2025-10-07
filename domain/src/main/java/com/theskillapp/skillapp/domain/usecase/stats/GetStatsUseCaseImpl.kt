package com.theskillapp.skillapp.domain.usecase.stats

import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.model.Statistic
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.repository.StatsRepository
import com.theskillapp.skillapp.shared.util.sumByLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetStatsUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository,
) : GetStatsUseCase {

    override fun getStats(criteria: SkillSelectionCriteria, dates: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return skillRepository.getSkills(criteria).flatMapLatest { skills ->
            statsRepository.getStats(skills.map { it.id }, dates)
        }
    }

    override fun getGroupedStats(criteria: SkillSelectionCriteria, interval: StatisticInterval, dates: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return getStats(criteria, dates).map { stats ->
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
}
