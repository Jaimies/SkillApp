package com.maxpoliakov.skillapp.ui.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.shortName

class DayFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.plusDays(value.toLong())
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }
}
