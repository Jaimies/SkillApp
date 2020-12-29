package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.util.time.toReadableTime
import java.time.Duration
import java.time.LocalDate

sealed class HistoryUiModel {
    data class Record(
        val id: Int,
        val name: String,
        val time: Duration,
        val timeString: String,
        val date: LocalDate
    ) : HistoryUiModel()

    data class Separator(
        val date: LocalDate,
        val time: Duration
    ) : HistoryUiModel()
}

fun Record.mapToPresentation(): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, name, time, time.toReadableTime(), date)
}
