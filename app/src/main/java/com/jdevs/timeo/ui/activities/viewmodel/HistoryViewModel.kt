package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.RemoteRepository

class HistoryViewModel : ListViewModel() {

    override val liveData get() = RemoteRepository.recordsLiveData

    init {

        RemoteRepository.setupRecordsSource { onLastItemReached.call() }
    }

    fun deleteRecord(id: String, record: Record) {

        RemoteRepository.deleteRecord(id, record.time, record.activityId)
    }
}
