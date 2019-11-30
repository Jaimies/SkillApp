package com.jdevs.timeo.viewmodel

import com.jdevs.timeo.livedata.RecordListLiveData
import com.jdevs.timeo.repository.FirestoreRecordListRepository
import com.jdevs.timeo.viewmodel.common.ItemListViewModel

class RecordListViewModel : ItemListViewModel() {

    var navigator: Navigator? = null

    private val recordsListRepository =
        FirestoreRecordListRepository { navigator?.onLastItemReached() }

    val recordsListLiveData get() = recordsListRepository.getLiveData() as RecordListLiveData?

    override fun onFragmentDestroyed() {
        navigator = null
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        recordsListRepository.deleteRecord(id, recordTime, activityId)
    }

    interface Repository {
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }

    interface Navigator : ItemListViewModel.Navigator
}
