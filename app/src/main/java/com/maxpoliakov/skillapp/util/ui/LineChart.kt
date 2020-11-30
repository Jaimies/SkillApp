package com.maxpoliakov.skillapp.util.ui

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import com.maxpoliakov.skillapp.util.charts.StatsEntries

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f

fun LineChart.setState(entries: StatsEntries?) {
    if (entries != null) {
        updateUI(entries)
    } else {
        this.data = null
    }
}

private fun LineChart.updateUI(entries: StatsEntries) {
    this.data = LineData(createDataSets(entries))
    setupAxises(entries)
    notifyDataSetChanged()
    invalidate()
}

private fun LineChart.createDataSets(entries: StatsEntries): List<LineDataSet> {
    return listOf(createDataSet(entries.entries))
}

private fun LineChart.createDataSet(entries: List<Entry>?): LineDataSet {
    val textColor = context.getTextColor()

    return LineDataSet(entries, "").apply {
        valueTextSize = VALUE_TEXT_SIZE
        valueFormatter = TimeFormatter()
        valueTextColor = textColor
        this.color = textColor
        lineWidth = LINE_WIDTH
        setDrawCircles(false)
        setDrawValues(false)
        lineWidth = 3f
        color = context.getPrimaryColor()
        isHighlightEnabled = false
    }
}

private fun LineChart.setupAxises(entries: StatsEntries) {
    axisLeft.axisMaximum = entries.getMaximumValue()
}
