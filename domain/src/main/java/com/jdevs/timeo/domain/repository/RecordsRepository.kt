package com.jdevs.timeo.domain.repository

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Record

interface RecordsRepository {
    val records: DataSource.Factory<Id, Record>

    suspend fun addRecord(record: Record)
    suspend fun deleteRecord(record: Record)
}
