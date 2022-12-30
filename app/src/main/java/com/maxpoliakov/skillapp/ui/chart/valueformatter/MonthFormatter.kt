package com.maxpoliakov.skillapp.ui.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.shortName

class MonthFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.plusMonths(value.toLong())
        return "${date.month.shortName}\n${date.year % 100}"
    }
}
