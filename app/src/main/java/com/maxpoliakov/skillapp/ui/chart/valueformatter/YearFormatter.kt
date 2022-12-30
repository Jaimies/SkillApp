package com.maxpoliakov.skillapp.ui.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.shared.util.EPOCH

class YearFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.plusYears(value.toLong())
        return date.year.toString()
    }
}
