package com.maxpoliakov.skillapp.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.model.RecordItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import kotlinx.coroutines.flow.map

class HistoryViewModel @ViewModelInject constructor(
    getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase
) : ViewModel() {

    val records = getRecords.run().map { data ->
        data.map { it.mapToPresentation() }
    }

    fun deleteRecord(record: RecordItem) = launchCoroutine {
        deleteRecord.run(record.mapToDomain())
    }
}
