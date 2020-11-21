package com.jdevs.timeo.data.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecordsRepository @Inject constructor(
    private val recordsDao: RecordsDao
) : RecordsRepository {

    override val records by lazy {
        recordsDao.getRecords().map(DBRecord::mapToDomain)
    }

    override suspend fun addRecord(record: Record) {
        recordsDao.insert(record.mapToDB())
    }

    override suspend fun deleteRecord(record: Record) {
        recordsDao.delete(record.mapToDB())
    }
}
