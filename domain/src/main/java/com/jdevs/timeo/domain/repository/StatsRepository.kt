package com.jdevs.timeo.domain.repository

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.Statistic
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getStats(activityId: Id): Flow<List<Statistic>>
    suspend fun addRecord(record: Record)
}
