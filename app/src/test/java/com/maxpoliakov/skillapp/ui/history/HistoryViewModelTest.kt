package com.maxpoliakov.skillapp.ui.history

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.StubGetHistoryUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.StubGetRecentCountUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.test.awaitData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class HistoryViewModelTest : StringSpec({
    val records = listOf(
        Record("", 4, 0, MeasurementUnit.Millis, 0, LocalDate.ofEpochDay(10)),
        Record("", 3, 0, MeasurementUnit.Millis, 1, LocalDate.ofEpochDay(9)),
        Record("", 2, 0, MeasurementUnit.Millis, 2, LocalDate.ofEpochDay(7)),
        Record("", 2, 0, MeasurementUnit.Millis, 3, LocalDate.ofEpochDay(7)),
    )

    "records maps the input records and adds the separators where needed" {
        val viewModel = HistoryViewModel()
        viewModel.getHistory = StubGetHistoryUseCase(records)
        viewModel.getRecentCount = StubGetRecentCountUseCase(200L)

        viewModel.records.first().awaitData() shouldBe listOf(
            Separator(LocalDate.ofEpochDay(10), Separator.Total(200, UiMeasurementUnit.Millis)),
            createUiRecord(0, LocalDate.ofEpochDay(10)),
            Separator(LocalDate.ofEpochDay(9), Separator.Total(200, UiMeasurementUnit.Millis)),
            createUiRecord(1, LocalDate.ofEpochDay(9)),
            Separator(LocalDate.ofEpochDay(7), Separator.Total(200, UiMeasurementUnit.Millis)),
            createUiRecord(2, LocalDate.ofEpochDay(7)),
            createUiRecord(3, LocalDate.ofEpochDay(7)),
        )
    }
})

private fun createUiRecord(id: Int, date: LocalDate): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, "", 0, UiMeasurementUnit.Millis, date, null)
}
