package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.remote.RemoteRepository
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class HistoryViewModel(private val repository: TimeoRepository) : ListViewModel() {

    override val liveData get() = RemoteRepository.recordsLiveData
    override val items = repository.getRecords() as LiveData<List<ViewType>>

    init {

        RemoteRepository.setupRecordsSource { onLastItemReached.call() }
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {

        repository.deleteRecord(record)
    }
}
