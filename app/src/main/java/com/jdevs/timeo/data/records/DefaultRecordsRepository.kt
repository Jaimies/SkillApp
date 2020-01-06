package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.domain.model.Record
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsRepository {

    val records: LiveData<*>?

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetRecordsMonitor()
}

@Singleton
class DefaultRecordsRepository @Inject constructor(
    remoteDataSource: RecordsRemoteDataSource,
    localDataSource: RecordsDataSource,
    authRepository: AuthRepository
) : Repository<RecordsRemoteDataSource, RecordsDataSource>(
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
