package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getStats(skillId: Id): Flow<List<Statistic>>
    suspend fun addRecord(record: Record)
}
