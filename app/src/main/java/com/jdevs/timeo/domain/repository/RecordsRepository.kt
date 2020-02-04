package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.domain.model.Record

interface RecordsRepository {

    val records: LiveData<PagedList<Record>>
    val recordsRemote: List<ItemsLiveData>

    suspend fun addRecord(record: Record): WriteBatch?

    suspend fun deleteRecord(record: Record): WriteBatch?

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)
}
