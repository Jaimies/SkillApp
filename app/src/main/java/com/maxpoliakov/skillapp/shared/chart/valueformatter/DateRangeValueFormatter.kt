package com.maxpoliakov.skillapp.shared.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.model.UiStatisticInterval

class DateRangeValueFormatter(private val interval: UiStatisticInterval): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val dateRange = interval.domainCounterpart.toDateRange(value.toLong())
        return interval.formatter.format(dateRange.start)
    }
}
