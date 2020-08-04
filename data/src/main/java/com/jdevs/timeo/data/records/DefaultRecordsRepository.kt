package com.jdevs.timeo.data.records

import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecordsRepository @Inject constructor(
    private val remoteDataSource: RecordsRemoteDataSource,
    private val localDataSource: RecordsLocalDataSource,
    authRepository: AuthRepository
) :
    Repository<RecordsDataSource>(remoteDataSource, localDataSource, authRepository),
    RecordsRepository {

    override val records get() = localDataSource.records

    override fun getRecordsFromRemote(fetchNewItems: Boolean) = remoteDataSource.getRecords(fetchNewItems)

    override suspend fun addRecord(record: Record) = currentDataSource.addRecord(record)
    override suspend fun deleteRecord(record: Record) = currentDataSource.deleteRecord(record)
}
