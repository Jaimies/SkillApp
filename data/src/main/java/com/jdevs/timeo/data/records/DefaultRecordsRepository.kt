package com.jdevs.timeo.data.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecordsRepository @Inject constructor(
    private val dataSource: RecordsDataSource
) : RecordsRepository {

    override val records get() = dataSource.records

    override suspend fun addRecord(record: Record) =
        dataSource.addRecord(record)

    override suspend fun deleteRecord(record: Record) =
        dataSource.deleteRecord(record)
}
