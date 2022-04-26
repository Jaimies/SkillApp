package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Record
import java.time.LocalDate

sealed class HistoryUiModel {
    data class Record(
        val id: Int,
        val name: String,
        val count: Long,
        val unit: UiMeasurementUnit,
        val date: LocalDate
    ) : HistoryUiModel()

    data class Separator(
        val date: LocalDate,
        val count: Long,
    ) : HistoryUiModel()
}

fun Record.mapToPresentation(): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, name, count, UiMeasurementUnit.from(unit), date)
}
