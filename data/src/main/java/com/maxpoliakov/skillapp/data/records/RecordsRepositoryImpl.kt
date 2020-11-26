package com.maxpoliakov.skillapp.data.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {

    private val _records by lazy {
        recordsDao.getRecords().map(DBRecord::mapToDomain)
    }

    override fun getRecords() = _records

    override suspend fun addRecord(record: Record) {
        recordsDao.insert(record.mapToDB())
    }

    override suspend fun deleteRecord(record: Record) {
        recordsDao.delete(record.mapToDB())
    }
}
