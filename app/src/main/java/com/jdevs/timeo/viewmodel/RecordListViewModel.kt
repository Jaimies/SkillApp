package com.jdevs.timeo.viewmodel

import com.jdevs.timeo.livedata.RecordListLiveData
import com.jdevs.timeo.repository.FirestoreRecordListRepository
import com.jdevs.timeo.viewmodel.common.ItemListViewModel

class RecordListViewModel : ItemListViewModel() {

    var navigator: Navigator? = null

    private val recordsRepository =
        FirestoreRecordListRepository { navigator?.onLastItemReached() }

    val recordsLiveData get() = recordsRepository.getLiveData() as RecordListLiveData?

    override fun onFragmentDestroyed() {
        navigator = null
        recordsRepository.onFragmentDestroyed()
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        recordsRepository.deleteRecord(id, recordTime, activityId)
    }

    interface Repository {
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }

    interface Navigator : ItemListViewModel.Navigator
}
