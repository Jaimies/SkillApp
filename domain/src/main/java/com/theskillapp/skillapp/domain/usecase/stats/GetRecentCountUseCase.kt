package com.theskillapp.skillapp.domain.usecase.stats

import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.model.StatisticInterval
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetRecentCountUseCase {
    fun getCountSinceStartOfInterval(criteria: SkillSelectionCriteria, interval: StatisticInterval): Flow<Long>
    fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Flow<Long>
    fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Flow<Long>
    fun getLast7DayCount(criteria: SkillSelectionCriteria): Flow<Long>
}
