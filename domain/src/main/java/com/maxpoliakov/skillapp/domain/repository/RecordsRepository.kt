package com.maxpoliakov.skillapp.domain.repository

import androidx.paging.DataSource
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record

interface RecordsRepository {
    fun getRecords(): DataSource.Factory<Id, Record>

    suspend fun addRecord(record: Record)
    suspend fun deleteRecord(record: Record)
}
