package com.maxpoliakov.skillapp.shared.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter

class IntegerFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value == 0f) return ""
        return value.toInt().toString()
    }
}