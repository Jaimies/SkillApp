package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.livedata.RecordsListLiveData

class RecordsListViewModel : ViewModel() {
    private val recordsListRepository = FirestoreRecordsListRepository()

    fun getRecordsListLiveData(): RecordsListLiveData? {
        return recordsListRepository.getRecordsListLiveData()
    }

    interface RecordsListRepository {
        fun getRecordsListLiveData(): RecordsListLiveData?
    }
}