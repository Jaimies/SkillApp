package com.jdevs.timeo.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.records.DeleteRecordUseCase
import com.jdevs.timeo.domain.usecase.records.GetRecordsUseCase
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.lifecycle.launchCoroutine

private const val RECORDS_PAGE_SIZE = 50

class HistoryViewModel @ViewModelInject constructor(
    getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ViewModel() {

    val records =
        getRecords.run().map(Record::mapToPresentation)
            .toLiveData(RECORDS_PAGE_SIZE)

    fun deleteRecord(record: RecordItem) = launchCoroutine {
        deleteRecord.run(record.mapToDomain())
    }
}
