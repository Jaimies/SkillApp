package com.theskillapp.skillapp.domain.usecase.stats

import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.repository.StatsRepository
import com.theskillapp.skillapp.domain.time.DateProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

class GetRecentCountUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository,
    private val dateProvider: DateProvider,
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(criteria: SkillSelectionCriteria, interval: StatisticInterval): Flow<Long> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()
        val startOfInterval = interval.atStartOfInterval(currentDate)
        return getCount(criteria, startOfInterval..currentDate)
    }

    override fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Flow<Long> {
        return skillRepository.getSkills(criteria).flatMapLatest {
            statsRepository.getCount(it.map { skill -> skill.id }, range)
        }
    }

    override fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Flow<Long> {
        return getCount(criteria, date..date)
    }

    override fun getLast7DayCount(criteria: SkillSelectionCriteria): Flow<Long> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()
        return getCount(criteria, currentDate.minusDays(6)..currentDate)
    }
}
