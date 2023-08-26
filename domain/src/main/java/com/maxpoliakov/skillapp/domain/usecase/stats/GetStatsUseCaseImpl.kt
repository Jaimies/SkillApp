package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.time.DateProvider
import com.maxpoliakov.skillapp.shared.util.sumByLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetStatsUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
    private val dateProvider: DateProvider,
) : GetStatsUseCase {

    override fun getStats(criteria: SkillSelectionCriteria, dates: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return skillRepository.getSkills(criteria).flatMapLatest { skills ->
            combine(skills.map { statsRepository.getStats(it.id, dates) }) {
                it.toList().flatten()
            }
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

    override fun getLast7DayCount(criteria: SkillSelectionCriteria): Flow<Long> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()

        return getStats(criteria, currentDate.minusDays(6)..currentDate).map { stats ->
            stats.sumByLong(Statistic::count)
        }
    }
}
