package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import kotlinx.coroutines.flow.Flow

interface GetRecentCountUseCase {
    fun getCountSinceStartOfInterval(criteria: SkillSelectionCriteria, interval: StatisticInterval): Flow<Long>
}
