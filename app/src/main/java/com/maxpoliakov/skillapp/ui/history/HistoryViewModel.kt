package com.maxpoliakov.skillapp.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.toLiveData
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.model.RecordItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine

private const val RECORDS_PAGE_SIZE = 50

class HistoryViewModel @ViewModelInject constructor(
    getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ViewModel() {

    val records = getRecords.run()
        .map(Record::mapToPresentation)
        .toLiveData(RECORDS_PAGE_SIZE)

    val isEmpty = records.map { it.isEmpty() }

    fun deleteRecord(record: RecordItem) = launchCoroutine {
        deleteRecord.run(record.mapToDomain())
    }
}
