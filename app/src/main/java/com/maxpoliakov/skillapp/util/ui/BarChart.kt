package com.maxpoliakov.skillapp.util.ui

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.ui.common.TimeFormatter

private const val VALUE_TEXT_SIZE = 12f

fun BarChart.setState(entries: List<BarEntry>?) {
    if (entries != null) {
        updateUI(entries)
    } else {
        this.data = null
    }
}

private fun BarChart.updateUI(entries: List<BarEntry>) {
    this.data = BarData(createDataSets(entries))
    this.data.barWidth = 0.3f
    notifyDataSetChanged()
    invalidate()
}

private fun BarChart.createDataSets(entries: List<BarEntry>): List<BarDataSet> {
    return listOf(createDataSet(entries))
}

private fun BarChart.createDataSet(entries: List<BarEntry>): BarDataSet {
    val textColor = context.getTextColor()

    return BarDataSet(entries, "").apply {
        valueTextSize = VALUE_TEXT_SIZE
        valueFormatter = TimeFormatter(context)
        valueTextColor = textColor
        this.color = textColor
        color = context.getPrimaryColor()
        isHighlightEnabled = false
    }
}
