package com.jdevs.timeo.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.usecase.records.DeleteRecordUseCase
import com.jdevs.timeo.domain.usecase.records.GetRecordsUseCase
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.lifecycle.mapOperation

private const val RECORDS_PAGE_SIZE = 50

class HistoryViewModel @ViewModelInject constructor(
    private val getRecords: GetRecordsUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    authRepository: AuthRepository
) : ListViewModel<RecordItem>(authRepository) {

    override val localLiveData =
        getRecords.records.map(Record::mapToPresentation)
            .toLiveData(RECORDS_PAGE_SIZE)

    override fun getRemoteLiveDatas(fetchNewItems: Boolean) =
        getRecords.getRecordsRemote(fetchNewItems)
            .mapOperation(Record::mapToPresentation)

    fun deleteRecord(record: RecordItem) = launchCoroutine {
        deleteRecord.run(record.mapToDomain())
    }
}
