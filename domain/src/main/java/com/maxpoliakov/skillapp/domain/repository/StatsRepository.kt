package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate

interface StatsRepository {
    fun getStats(skillId: Id): Flow<List<Statistic>>
    fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>>
    suspend fun addRecord(record: Record)
    suspend fun getTimeAtDate(date: LocalDate): Duration
}
