package com.maxpoliakov.skillapp.util.charts

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

fun PieChart.setup() {
    setHoleColor(Color.TRANSPARENT)
    holeRadius = 52.5f
    transparentCircleRadius = 0f
    description.isEnabled = false
    legend.isEnabled = false
}

fun PieChart.setData(entries: List<PieEntry>) {
    val dataSet = PieDataSet(entries, "").apply {
        setColors(ColorTemplate.COLORFUL_COLORS, 255)
        sliceSpace = 15f

        setDrawValues(false)
    }

    data = PieData(dataSet)
    invalidate()
}
