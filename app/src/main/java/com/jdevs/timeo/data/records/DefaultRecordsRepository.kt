package com.jdevs.timeo.data.records

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecordsRepository @Inject constructor(
    remoteDataSource: RecordsRemoteDataSource,
    localDataSource: RecordsDataSource,
    authRepository: AuthRepository
) : Repository<RecordsDataSource, RecordsRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), RecordsRepository {

    override val records get() = currentDataSource.records

    override suspend fun addRecord(record: Record) = currentDataSource.addRecord(record)

    override suspend fun deleteRecord(record: Record) = currentDataSource.deleteRecord(record)

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) =
        performOnRemoteSuspend {

            it.renameRecords(activityId, newName, batch)
        }

    override fun resetMonitor() = performOnRemote { it.resetMonitor() }
}
