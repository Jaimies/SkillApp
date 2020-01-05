package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record

interface RecordsRemoteDataSource {

    val records: ItemsLiveData?

    suspend fun addRecord(record: Record): WriteBatch

    suspend fun deleteRecord(record: Record): WriteBatch

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetRecordsMonitor()
}
