package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.livedata.RecordsListLiveData
import com.jdevs.timeo.repositories.FirestoreRecordsListRepository

class RecordsListViewModel : ViewModel() {
    private val recordsListRepository =
        FirestoreRecordsListRepository()

    fun getRecordsListLiveData(): RecordsListLiveData? {
        return recordsListRepository.getRecordsListLiveData()
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        recordsListRepository.deleteRecord(id, recordTime, activityId)
    }

    interface RecordsListRepository {
        fun getRecordsListLiveData(): RecordsListLiveData?
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }
}
