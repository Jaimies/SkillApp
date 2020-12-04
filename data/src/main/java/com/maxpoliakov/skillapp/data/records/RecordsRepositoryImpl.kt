package com.maxpoliakov.skillapp.data.records

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val pagingConfig = PagingConfig(pageSize = 50)

@Singleton
class RecordsRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {

    private val _records by lazy {
        Pager(config = pagingConfig) {
            recordsDao.getRecords()
        }.flow.map { data ->
            data.map { it.mapToDomain() }
        }
    }

    override fun getRecords() = _records

    override suspend fun getRecord(id: Int): Record {
        return recordsDao.getRecordById(id).mapToDomain()
    }

    override suspend fun addRecord(record: Record) {
        recordsDao.insert(record.mapToDB())
    }

    override suspend fun deleteRecord(record: Record) {
        recordsDao.delete(record.mapToDB())
    }
}
