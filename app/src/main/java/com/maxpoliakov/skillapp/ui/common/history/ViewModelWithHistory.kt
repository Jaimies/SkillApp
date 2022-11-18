package com.maxpoliakov.skillapp.ui.common.history

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.usecase.records.GetHistoryUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

abstract class ViewModelWithHistory : ViewModel() {
    @Inject
    lateinit var getHistory: GetHistoryUseCase

    protected abstract val selectionCriteria: SkillSelectionCriteria
    protected abstract val unitForDailyTotals: Flow<MeasurementUnit>

    val records by lazy {
        getHistory.getRecords(selectionCriteria).map { data ->
            data.map { it.mapToPresentation() }.withSeparators()
        }
    }

    private suspend fun getCountAtDate(unit: MeasurementUnit, date: LocalDate): Long {
        return getHistory.getCount(
            selectionCriteria.withUnit(unit),
            date,
        )
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
        return Separator(record.date, getTotal(record.date))
    }

    private suspend fun getTotal(date: LocalDate): Separator.Total {
        val unit = unitForDailyTotals.first()
        return Separator.Total(getCountAtDate(unit, date), unit.mapToUI())
    }
}
