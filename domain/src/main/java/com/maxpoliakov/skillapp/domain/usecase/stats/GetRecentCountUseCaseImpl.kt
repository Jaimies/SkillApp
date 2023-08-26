package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.domain.time.DateProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

open class GetRecentCountUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository,
    private val dateProvider: DateProvider,
) : GetRecentCountUseCase {

    override fun getCountSinceStartOfInterval(criteria: SkillSelectionCriteria, interval: StatisticInterval): Flow<Long> {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()
        val startOfInterval = interval.atStartOfInterval(currentDate)
        return this.getCount(criteria, startOfInterval..currentDate)
    }

    override fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Flow<Long> {
        return skillRepository.getSkills(criteria).flatMapLatest {
            statsRepository.getCount(it.map { skill -> skill.id }, range)
        }
    }

    override fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Flow<Long> {
        return getCount(criteria, date..date)
    }
}
