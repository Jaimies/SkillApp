package com.jdevs.timeo.ui.activities

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData get() = repository.activitiesLiveData
    val navigateToAddEdit = SingleLiveEvent<Any>()

    init {

        repository.resetActivitiesMonitor()
    }

    fun createRecord(record: Record) = viewModelScope.launch {

        repository.addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
