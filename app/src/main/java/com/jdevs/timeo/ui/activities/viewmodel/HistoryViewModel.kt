package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.RemoteRepository

class HistoryViewModel : ListViewModel() {

    val recordsLiveData get() = RemoteRepository.recordsLiveData

    init {

        RemoteRepository.setupRecordsSource { onLastItemReached.call() }
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        RemoteRepository.deleteRecord(id, recordTime, activityId)
    }
}
