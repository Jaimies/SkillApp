package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.domain.model.Record

interface RecordsRepository {

    val records: LiveData<*>?

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetMonitor()
}