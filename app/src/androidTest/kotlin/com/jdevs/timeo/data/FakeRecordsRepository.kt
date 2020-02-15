package com.jdevs.timeo.data

import com.jdevs.timeo.ItemDataSource
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.util.createLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRecordsRepository @Inject constructor() : RecordsRepository {

    private val recordsList = mutableListOf<Record>()
    override val records = ItemDataSource.Factory(recordsList)

    override fun getRecordsRemote(fetchNewItems: Boolean) =
        listOf(createLiveData<Operation<Record>>())

    override suspend fun addRecord(record: Record) {

        recordsList.add(record)
        notifyObservers()
    }

    override suspend fun deleteRecord(record: Record) {

        recordsList.remove(record)
        notifyObservers()
    }

    fun reset() {

        recordsList.clear()
        notifyObservers()
    }

    private fun notifyObservers() = records
}
