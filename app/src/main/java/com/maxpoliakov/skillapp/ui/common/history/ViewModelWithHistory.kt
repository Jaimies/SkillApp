package com.maxpoliakov.skillapp.ui.common.history

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.mapToPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class ViewModelWithHistory : ViewModel() {
    protected abstract val recordPagingData: Flow<PagingData<Record>>

    val records by lazy {
        recordPagingData.map { data ->
            data.map { it.mapToPresentation() }.withSeparators()
        }
    }

    private fun PagingData<HistoryUiModel.Record>.withSeparators(): PagingData<HistoryUiModel> {
        return this.insertSeparators { record, record2 ->
            if (record2 != null && record?.date != record2.date)
                HistoryUiModel.Separator(record2.date)
            else null
        }
    }
}
