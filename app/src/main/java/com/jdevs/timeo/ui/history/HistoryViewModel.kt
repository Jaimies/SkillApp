package com.jdevs.timeo.ui.history

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.records.DeleteRecordUseCase
import com.jdevs.timeo.domain.usecase.records.GetRecordsUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ListViewModel() {

    override val liveData get() = getRecords()

    init {

        getRecords.resetRecords()
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {

        deleteRecord.invoke(record)
    }
}
