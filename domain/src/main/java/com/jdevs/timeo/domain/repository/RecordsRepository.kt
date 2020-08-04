package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record

interface RecordsRepository {
    val records: DataSource.Factory<Int, Record>

    fun getRecordsFromRemote(fetchNewItems: Boolean): List<LiveData<Operation<Record>>>
    suspend fun addRecord(record: Record)
    suspend fun deleteRecord(record: Record)
}
