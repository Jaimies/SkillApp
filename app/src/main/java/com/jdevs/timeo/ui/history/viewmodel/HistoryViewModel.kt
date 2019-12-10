package com.jdevs.timeo.ui.history.viewmodel

import com.jdevs.timeo.common.viewmodel.ItemListViewModel
import com.jdevs.timeo.data.livedata.RecordListLiveData
import com.jdevs.timeo.data.source.RecordsRepository

class HistoryViewModel : ItemListViewModel() {

    var navigator: Navigator? = null
    val recordsLiveData get() = repository.getLiveData() as RecordListLiveData?

    override val repository =
        RecordsRepository { navigator?.onLastItemReached() }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        repository.deleteRecord(id, recordTime, activityId)
    }

    interface Navigator : ItemListViewModel.Navigator
}
