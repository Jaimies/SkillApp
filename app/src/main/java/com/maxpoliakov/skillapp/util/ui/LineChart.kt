package com.maxpoliakov.skillapp.util.ui

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import kotlin.math.ceil

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f

fun LineChart.setState(entries: List<Entry>?) {
    if (entries != null) {
        updateUI(entries)
    } else {
        this.data = null
    }
}

private fun LineChart.updateUI(entries: List<Entry>) {
    this.data = LineData(createDataSets(entries))
    setupAxises(entries)
    notifyDataSetChanged()
    invalidate()
}

private fun LineChart.createDataSets(entries: List<Entry>): List<LineDataSet> {
    return listOf(createDataSet(entries))
}

private fun LineChart.createDataSet(entries: List<Entry>): LineDataSet {
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

private fun LineChart.setupAxises(entries: List<Entry>) {
    axisLeft.axisMaximum = entries.getMaximumValue()
}

private const val HOURS_DIVIDER = 120

fun List<Entry>.getMaximumValue(): Float {
    val maxValue = getHighestEntryValue()
    if (maxValue <= HOURS_DIVIDER) return maxValue.ceilTo(60)
    return maxValue.ceilTo(HOURS_DIVIDER)
}

private fun Float.ceilTo(to: Int) = ceil(this / to) * to
private fun List<Entry>.getHighestEntryValue() = this.maxBy(Entry::getY)!!.y
