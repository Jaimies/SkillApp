package com.jdevs.timeo.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jdevs.timeo.R

private const val RIGHT_OFFSET_MULTIPLIER = 3

fun LineChart.setup(textSize: Float, offset: Float) {

    legend.isEnabled = false
    axisRight.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false

    setExtraOffsets(offset, 0f, offset * RIGHT_OFFSET_MULTIPLIER, offset)

    data?.setValueTextSize(textSize)

    getPaint(Chart.PAINT_INFO).textSize = context.dpToPx(textSize.toInt()).toFloat()
    setNoDataTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary))
    setNoDataText(context.getString(R.string.no_data))

    axisLeft.axisMinimum = 0f
    xAxis.textSize = textSize
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)

    axisLeft.textSize = textSize
}

fun Context.createLineData(entries: List<Entry>?): LineData? {

    if (entries == null) {

        return null
    }

    val dataset = LineDataSet(entries, "")

    dataset.setDrawCircles(false)
    dataset.setDrawValues(false)
    dataset.color = ContextCompat.getColor(this, R.color.colorTextPrimary)

    return LineData(dataset)
}
