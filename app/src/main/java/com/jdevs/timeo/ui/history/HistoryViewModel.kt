package com.jdevs.timeo.ui.history

import androidx.lifecycle.viewModelScope
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.records.DeleteRecordUseCase
import com.jdevs.timeo.domain.usecase.records.GetRecordsUseCase
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.mapTo
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RECORDS_PAGE_SIZE = 50

class HistoryViewModel @Inject constructor(
    private val getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ListViewModel<RecordItem>() {

    override val localLiveData
        get() = getRecords.records.map(Record::mapToPresentation).toLiveData(RECORDS_PAGE_SIZE)

    override val remoteLiveDatas get() = getRecords.recordsRemote.mapTo(Record::mapToPresentation)

    fun deleteRecord(record: RecordItem) = viewModelScope.launch {

        deleteRecord.invoke(record.mapToDomain())
    }
}
