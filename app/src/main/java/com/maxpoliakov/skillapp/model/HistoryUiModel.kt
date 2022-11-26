package com.maxpoliakov.skillapp.model

import android.content.Context
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import java.time.LocalDate
import java.time.LocalDateTime

sealed class HistoryUiModel {
    data class Record(
        val id: Int,
        val name: String,
        val count: Long,
        val unit: UiMeasurementUnit,
        val date: LocalDate,
        val dateTimeRange: ClosedRange<LocalDateTime>?,
    ) : HistoryUiModel()

    data class Separator(
        val date: LocalDate,
        val total: Total,
    ) : HistoryUiModel() {

        data class Total(val count: Long, val unit: UiMeasurementUnit) {
            fun format(context: Context): String {
                if (count == 0L) return ""
                return unit.toShortString(count, context)
            }
        }
    }
}

fun Record.mapToPresentation(): HistoryUiModel.Record {
    return HistoryUiModel.Record(id, name, count, unit.mapToUI(), date, dateTimeRange)
}
