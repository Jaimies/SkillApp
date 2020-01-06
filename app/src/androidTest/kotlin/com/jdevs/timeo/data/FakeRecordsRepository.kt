package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.model.Record
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("EmptyFunctionBlock")
class FakeRecordsRepository @Inject constructor() : RecordsRepository {

    private val recordsList = mutableListOf<Record>()
    override val records = MutableLiveData(recordsList.asPagedList())

    override suspend fun addRecord(record: Record): WriteBatch? {

        recordsList.add(record)
        notifyObservers()
        return null
    }

    override suspend fun deleteRecord(record: Record): WriteBatch? {

        recordsList.remove(record)
        notifyObservers()
        return null
    }

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) {}

    fun reset() {

        recordsList.clear()
        notifyObservers()
    }

    override fun resetRecordsMonitor() {}

    private fun notifyObservers() = records.postValue(recordsList.asPagedList())
}
