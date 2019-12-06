package com.jdevs.timeo.ui.history.viewmodel

import com.jdevs.timeo.api.livedata.RecordListLiveData
import com.jdevs.timeo.api.repository.firestore.RecordsRepository
import com.jdevs.timeo.common.viewmodel.ItemListViewModel

class HistoryViewModel : ItemListViewModel() {
    var navigator: Navigator? = null
    val recordsLiveData get() = repository.getLiveData() as RecordListLiveData?

    private lateinit var repository: RecordsRepository

    override fun onFragmentDestroyed() {
        navigator = null
    }

    override fun setupRepository() {
        repository = RecordsRepository { navigator?.onLastItemReached() }
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {
        repository.deleteRecord(id, recordTime, activityId)
    }

    interface Repository {
        fun deleteRecord(id: String, recordTime: Long, activityId: String)
    }

    interface Navigator : ItemListViewModel.Navigator
}
