package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.livedata.RecordsListLiveData
import com.jdevs.timeo.repositories.FirestoreRecordsListRepository

class RecordsListViewModel : ViewModel() {
    private val _isEmpty = MutableLiveData(true)
    private val _isLoaded = MutableLiveData(true)

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    val isLoaded: LiveData<Boolean> get() = _isLoaded

    fun onLoaded() {
        _isLoaded.value = true
    }

    fun setLength(length: Int) {
        if (length > 0) {
            _isEmpty.value = false
        }
    }

    private val recordsListRepository =
        FirestoreRecordsListRepository()

    val recordsListLiveData get() = recordsListRepository.getRecordsListLiveData()

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        recordsListRepository.deleteRecord(id, recordTime, activityId)
    }

    interface RecordsListRepository {
        fun getRecordsListLiveData(): RecordsListLiveData?
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }
}
