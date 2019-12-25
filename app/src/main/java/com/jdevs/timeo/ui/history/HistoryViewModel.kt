package com.jdevs.timeo.ui.history

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData get() = repository.recordsLiveData

    init {

        repository.resetRecordsMonitor()
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {

        repository.deleteRecord(record)
    }
}
