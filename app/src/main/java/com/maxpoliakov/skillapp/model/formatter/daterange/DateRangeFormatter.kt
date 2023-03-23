package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import java.time.LocalDate

abstract class DateRangeFormatter {
    val valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return formatDate(value.toLong())
        }
    }

    abstract val currentDateRangeStringResId: Int
    abstract val interval: StatisticInterval

    fun formatDate(value: Long): String {
        val dateRange = interval.toDateRange(value)
        return format(dateRange.start)
    }

    fun formatDateRange(value: Long, context: Context): String {
        val dateRange = interval.toDateRange(value)
        return format(dateRange, context)
    }

    fun format(range: ClosedRange<LocalDate>, context: Context): String {
       if (range == interval.getCurrentDateRange()) {
            return context.getString(currentDateRangeStringResId)
        }

        return _format(range, context)
    }

    abstract fun format(date: LocalDate): String
    protected abstract fun _format(range: ClosedRange<LocalDate>, context: Context): String
}
