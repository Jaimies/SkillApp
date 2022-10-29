package com.maxpoliakov.skillapp.ui.common.history

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.mapToPresentation
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

abstract class ViewModelWithHistory: ViewModel() {
    @Inject
    lateinit var getHistory: GetHistoryUseCase

    protected abstract val selectionCriteria: SkillSelectionCriteria

    val records by lazy {
        getHistory.getRecords(selectionCriteria).map { data ->
            data.map { it.mapToPresentation() }.withSeparators()
        }
    }

    private suspend fun getTimeAtDate(date: LocalDate): Duration {
        return getHistory.getTimeAtDate(selectionCriteria, date)
    }

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
