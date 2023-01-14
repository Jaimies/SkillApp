package com.maxpoliakov.skillapp.ui.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter

class CountFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value == 0f) return ""
        return value.toInt().toString()
    }
}
