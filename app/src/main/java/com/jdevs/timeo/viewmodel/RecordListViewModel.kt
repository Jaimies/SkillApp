package com.jdevs.timeo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.livedata.RecordListLiveData
import com.jdevs.timeo.repository.FirestoreRecordListRepository

class RecordListViewModel : ViewModel() {
    private val _isEmpty = MutableLiveData(true)
    private val _isLoaded = MutableLiveData(false)

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
        FirestoreRecordListRepository()

    val recordsListLiveData get() = recordsListRepository.getRecordsListLiveData()

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        recordsListRepository.deleteRecord(id, recordTime, activityId)
    }

    interface Repository {
        fun getRecordsListLiveData(): RecordListLiveData?
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }
}
