package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record

interface RecordsRepository {

    val records: DataSource.Factory<Int, Record>
    val recordsRemote: List<LiveData<Operation<Record>>>

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)
}
