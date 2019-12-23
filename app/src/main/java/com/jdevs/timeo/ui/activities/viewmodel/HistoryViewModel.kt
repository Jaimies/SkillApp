package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class HistoryViewModel(private val repository: TimeoRepository) : ListViewModel() {

    override val liveData get() = repository.recordsLiveData
    override val items = repository.records as LiveData<List<ViewType>>

    fun deleteRecord(record: Record) = viewModelScope.launch {

        repository.deleteRecord(record)
    }
}
