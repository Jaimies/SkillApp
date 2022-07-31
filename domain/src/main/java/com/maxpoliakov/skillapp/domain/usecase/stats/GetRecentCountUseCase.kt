package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import kotlinx.coroutines.flow.Flow

interface GetRecentCountUseCase {
    fun getCountToday(id: Id): Flow<Long>
    fun getCountThisWeek(id: Id): Flow<Long>
}
