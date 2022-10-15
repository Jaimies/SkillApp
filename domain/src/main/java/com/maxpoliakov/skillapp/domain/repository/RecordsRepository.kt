package com.maxpoliakov.skillapp.domain.repository

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SelectionCriteria
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {
    fun getRecords(criteria: SelectionCriteria): Flow<PagingData<Record>>
    suspend fun getRecord(id: Int): Record?
    suspend fun addRecord(record: Record): Long
    suspend fun deleteRecord(record: Record)
}
