package com.jdevs.timeo.data.source

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsRepositoryImpl @Inject constructor(
    remoteDataSource: RecordsRemoteDataSource,
    localDataSource: RecordsDataSource,
    authRepository: AuthRepository
) : BaseRepository<RecordsRemoteDataSource, RecordsDataSource>(
    remoteDataSource, localDataSource, authRepository
), RecordsRepository {

    override val records get() = currentDataSource.records

    override suspend fun addRecord(record: Record) = currentDataSource.addRecord(record)

    override suspend fun deleteRecord(record: Record) = currentDataSource.deleteRecord(record)

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) =
        performOnRemoteSuspend {

            it.renameRecords(activityId, newName, batch)
        }

    override fun resetRecordsMonitor() = performOnRemote { it.resetRecordsMonitor() }
}
