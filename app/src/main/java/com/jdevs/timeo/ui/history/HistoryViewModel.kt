package com.jdevs.timeo.ui.history

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.usecases.DeleteRecordUseCase
import com.jdevs.timeo.usecases.GetRecordsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getRecordsUseCase: GetRecordsUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
) : ListViewModel() {

    override val liveData get() = getRecordsUseCase.records

    init {

        getRecordsUseCase.resetRecords()
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {

        deleteRecordUseCase.deleteRecord(record)
    }
}
