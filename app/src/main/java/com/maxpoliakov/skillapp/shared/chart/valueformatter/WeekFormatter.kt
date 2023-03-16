package com.maxpoliakov.skillapp.shared.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.shortName

class WeekFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.atStartOfWeek().plusWeeks(value.toLong())
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }
}
