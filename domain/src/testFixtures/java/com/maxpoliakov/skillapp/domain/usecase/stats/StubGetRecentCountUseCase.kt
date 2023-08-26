package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class StubGetRecentCountUseCase(countToReturn: Long): GetRecentCountUseCase {
    private val flow = flowOf(countToReturn)

    override fun getCountSinceStartOfInterval(criteria: SkillSelectionCriteria, interval: StatisticInterval) = flow
    override fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>) = flow
    override fun getCount(criteria: SkillSelectionCriteria, date: LocalDate) = flow
    override fun getLast7DayCount(criteria: SkillSelectionCriteria) = flow
}
