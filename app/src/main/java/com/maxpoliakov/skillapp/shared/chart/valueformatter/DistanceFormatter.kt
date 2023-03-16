package com.maxpoliakov.skillapp.shared.chart.valueformatter

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

class DistanceFormatter(private val context: Context) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value == 0f) return ""
        return UiMeasurementUnit.Meters.toString(value.toLong(), context)
    }
}
