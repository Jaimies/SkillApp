package com.theskillapp.skillapp.shared.history

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.usecase.records.GetHistoryUseCase
import com.theskillapp.skillapp.domain.usecase.stats.GetRecentCountUseCase
import com.theskillapp.skillapp.model.HistoryUiModel
import com.theskillapp.skillapp.model.HistoryUiModel.Separator
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.theskillapp.skillapp.model.mapToPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

abstract class ViewModelWithHistory : ViewModel() {
    @Inject
    lateinit var getHistory: GetHistoryUseCase

    @Inject
    lateinit var getRecentCount: GetRecentCountUseCase

    protected abstract val selectionCriteria: SkillSelectionCriteria
    protected abstract val unitForDailyTotals: Flow<MeasurementUnit<*>>

    val records by lazy {
        getHistory.getRecords(selectionCriteria).map { data ->
            data.map { it.mapToPresentation() }.withSeparators()
        }
    }

    private fun PagingData<HistoryUiModel.Record>.withSeparators(): PagingData<HistoryUiModel> {
        return this.insertSeparators(generator = ::createSeparatorIfNeeded)
    }

    private suspend fun createSeparatorIfNeeded(
        record: HistoryUiModel.Record?,
        record2: HistoryUiModel.Record?
    ): Separator? {
        if (record2 != null && record?.date != record2.date)
            return createSeparator(record2)

        return null
    }

    private suspend fun createSeparator(record: HistoryUiModel.Record): Separator {
        return Separator(record.date, getTotal(record.date).first())
    }

    private fun getTotal(date: LocalDate): Flow<Separator.Total> {
        return unitForDailyTotals.flatMapLatest { unit ->
            getCountAtDate(unit, date).map { count ->
                Separator.Total(count, unit.mapToUI())
            }
        }
    }

    private fun getCountAtDate(unit: MeasurementUnit<*>, date: LocalDate): Flow<Long> {
        return getRecentCount.getCount(selectionCriteria.withUnit(unit), date)
    }
}
