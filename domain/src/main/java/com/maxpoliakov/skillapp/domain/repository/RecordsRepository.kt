package com.maxpoliakov.skillapp.domain.repository

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {
    fun getRecordsBySkillIds(skillIds: List<Id>): Flow<PagingData<Record>>
    suspend fun getRecord(id: Int): Record?
    suspend fun addRecord(record: Record): Long
    suspend fun deleteRecord(record: Record)
}
