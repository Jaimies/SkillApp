package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.RecordsDataSource

interface RecordsRemoteDataSource : RecordsDataSource {

    override val records: ItemsLiveData?

    override suspend fun addRecord(record: Record): WriteBatch

    override suspend fun deleteRecord(record: Record): WriteBatch

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetRecordsMonitor()
}
