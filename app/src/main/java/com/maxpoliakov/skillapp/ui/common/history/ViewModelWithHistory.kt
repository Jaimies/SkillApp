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
import java.time.Duration
import java.time.LocalDate

abstract class ViewModelWithHistory : ViewModel() {
    protected abstract val recordPagingData: Flow<PagingData<Record>>

    val records by lazy {
        recordPagingData.map { data ->
            data.map { it.mapToPresentation() }.withSeparators()
        }
    }

    protected abstract suspend fun getTimeAtDate(date: LocalDate): Duration

    private fun PagingData<HistoryUiModel.Record>.withSeparators(): PagingData<HistoryUiModel> {
        return this.insertSeparators(generator = ::createSeparatorIfNeeded)
    }

    private suspend fun createSeparatorIfNeeded(
        record: HistoryUiModel.Record?,
        record2: HistoryUiModel.Record?
    ): HistoryUiModel.Separator? {
        if (record2 != null && record?.date != record2.date)
            return createSeparator(record2)

        return null
    }

    private suspend fun createSeparator(record: HistoryUiModel.Record): HistoryUiModel.Separator {
        return HistoryUiModel.Separator(record.date, getTimeAtDate(record.date))
    }
}
