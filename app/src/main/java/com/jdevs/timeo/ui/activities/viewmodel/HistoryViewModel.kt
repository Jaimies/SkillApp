package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.RemoteRepository

class HistoryViewModel : ListViewModel() {

    var navigator: Navigator? = null
    val recordsLiveData get() = RemoteRepository.recordsLiveData

    init {

        RemoteRepository.setupRecordsSource { navigator?.onLastItemReached() }
    }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        RemoteRepository.deleteRecord(id, recordTime, activityId)
    }

    interface Navigator : ListViewModel.Navigator
}
