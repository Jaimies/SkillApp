package com.jdevs.timeo.data.source

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSource
import javax.inject.Inject

class RecordsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RecordsRemoteDataSource,
    private val localDataSource: TimeoDataSource,
    authRepository: AuthRepository
) : BaseRepository(authRepository), RecordsRepository {

    private val currentDataSource get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val records get() = currentDataSource.records

    override suspend fun addRecord(record: Record) = currentDataSource.addRecord(record)

    override suspend fun deleteRecord(record: Record) = currentDataSource.deleteRecord(record)

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) {

        if (isUserSignedIn) {

            remoteDataSource.renameRecords(activityId, newName, batch)
        }
    }

    override fun resetRecordsMonitor() = performOnRemote { it.resetRecordsMonitor() }
}
