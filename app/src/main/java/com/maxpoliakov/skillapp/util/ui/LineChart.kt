package com.maxpoliakov.skillapp.util.ui

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import kotlin.math.ceil

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 3f

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
        valueFormatter = TimeFormatter(context)
        valueTextColor = textColor
        this.color = textColor
        lineWidth = LINE_WIDTH
        setDrawCircles(false)
        setDrawValues(false)
        color = context.getPrimaryColor()
        isHighlightEnabled = false
    }
}

private fun LineChart.setupAxises(entries: List<Entry>) {
    axisLeft.axisMaximum = entries.getAxisMaximum()
}

private const val HOURS_DIVIDER = 3600
private const val HOURS_DIVIDER_SMALL = 1800
private const val HOURS_BREAKPOINT = 7200

fun List<Entry>.getAxisMaximum(): Float = getAxisMaximum(getHighestValue())

fun getAxisMaximum(maxEntryValue: Float): Float {
    if (maxEntryValue <= HOURS_BREAKPOINT) return maxEntryValue.ceilTo(HOURS_DIVIDER_SMALL)
    return maxEntryValue.ceilTo(HOURS_DIVIDER)
}

private fun Float.ceilTo(to: Int) = ceil(this / to) * to
private fun List<Entry>.getHighestValue() = this.maxBy(Entry::getY)!!.y
