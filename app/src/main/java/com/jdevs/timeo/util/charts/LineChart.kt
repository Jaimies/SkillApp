package com.jdevs.timeo.util.charts

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.TimeFormatter

private const val RIGHT_OFFSET = 3
private const val TOP_OFFSET = 2
private const val X_AXIS_SPACE = 0.5f
private const val Y_AXIS_LABEL_COUNT = 5
private const val Y_AXIS_GRANULARITY = 0.1f

fun LineChart.setup(textSize: Float, offset: Float) {

    legend.isEnabled = false
    axisRight.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false

    setExtraOffsets(offset, offset * TOP_OFFSET, offset * RIGHT_OFFSET, offset)

    data?.setValueTextSize(textSize)

    setNoDataTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary))
    setNoDataText(context.getString(R.string.no_data))

    axisLeft.axisMinimum = 0f
    axisLeft.labelCount = Y_AXIS_LABEL_COUNT
    axisLeft.textSize = textSize
    axisLeft.valueFormatter = TimeFormatter()
    axisLeft.isGranularityEnabled = true
    axisLeft.granularity = Y_AXIS_GRANULARITY

    xAxis.spaceMax = X_AXIS_SPACE
    xAxis.spaceMin = X_AXIS_SPACE
    xAxis.textSize = textSize
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
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
