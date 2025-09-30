package com.theskillapp.skillapp.domain.repository

import androidx.paging.PagingData
import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {
    fun getRecordsBySkillIds(skillIds: List<Id>): Flow<PagingData<Record>>
    fun getRecord(id: Int): Flow<Record?>
    fun getLatestRecordForSkillWithId(id: Id): Flow<Record?>

    suspend fun addRecord(record: Record): Long
    suspend fun deleteRecord(record: Record)
}
