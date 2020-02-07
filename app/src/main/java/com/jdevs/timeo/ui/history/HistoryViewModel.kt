package com.jdevs.timeo.ui.history

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.records.DeleteRecordUseCase
import com.jdevs.timeo.domain.usecase.records.GetRecordsUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.ui.model.RecordItem
import com.jdevs.timeo.ui.model.mapToDomain
import com.jdevs.timeo.ui.model.mapToPresentation
import com.jdevs.timeo.util.PagingConstants.RECORDS_PAGE_SIZE
import com.jdevs.timeo.util.mapTo
import com.jdevs.timeo.util.toPagedList
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ListViewModel<RecordItem>() {

    override val localLiveData
        get() = getRecords.records.toPagedList(RECORDS_PAGE_SIZE, Record::mapToPresentation)

    override val remoteLiveDatas get() = getRecords.recordsRemote.mapTo(Record::mapToPresentation)

    fun deleteRecord(record: RecordItem) = viewModelScope.launch {

        deleteRecord.invoke(record.mapToDomain())
    }
}
