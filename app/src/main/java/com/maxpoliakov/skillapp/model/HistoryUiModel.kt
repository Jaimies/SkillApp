package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.util.time.getFriendlyTime
import java.time.LocalDate

sealed class HistoryUiModel {
    data class Record(
        val id: Int,
        val name: String,
        val time: String,
        val date: LocalDate
    ) : HistoryUiModel()

    data class Separator(val date: LocalDate) : HistoryUiModel()
}

fun Record.mapToPresentation(): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, name, getFriendlyTime(time), timestamp.toLocalDate())
}
