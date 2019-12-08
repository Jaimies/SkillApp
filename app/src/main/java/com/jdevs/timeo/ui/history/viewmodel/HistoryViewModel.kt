package com.jdevs.timeo.ui.history.viewmodel

import com.jdevs.timeo.api.livedata.RecordListLiveData
import com.jdevs.timeo.api.repository.firestore.RecordsRepository
import com.jdevs.timeo.common.viewmodel.ItemListViewModel

class HistoryViewModel : ItemListViewModel() {

    var navigator: Navigator? = null
    val recordsLiveData get() = repository.getLiveData() as RecordListLiveData?

    override val repository = RecordsRepository { navigator?.onLastItemReached() }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        repository.deleteRecord(id, recordTime, activityId)
    }

    interface Navigator : ItemListViewModel.Navigator
}
