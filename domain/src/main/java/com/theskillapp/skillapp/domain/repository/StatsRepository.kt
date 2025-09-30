package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Statistic
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatsRepository {
    fun getCount(id: Id, range: ClosedRange<LocalDate>): Flow<Long>
    fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>): Flow<Long>

    fun getStats(skillId: Id, range: ClosedRange<LocalDate>): Flow<List<Statistic>>
    fun getStats(skillIds: List<Id>, range: ClosedRange<LocalDate>): Flow<List<Statistic>>

    suspend fun addRecord(record: Record)
}
