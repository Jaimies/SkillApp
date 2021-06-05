package com.maxpoliakov.skillapp.util.charts

import androidx.core.graphics.ColorUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart.PAINT_INFO
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.utils.Utils
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import com.maxpoliakov.skillapp.ui.common.WeekDayFormatter
import com.maxpoliakov.skillapp.util.ui.getTextColor
import com.maxpoliakov.skillapp.util.ui.sp

private const val LEFT_OFFSET = 15f
private const val TOP_OFFSET = 27f
private const val RIGHT_OFFSET = 20f
private const val BOTTOM_OFFSET = 30f

private const val X_AXIS_SPACE_MIN = 0.4f
private const val X_AXIS_SPACE_MAX = 0.4f
private const val X_AXIS_Y_OFFSET = 10f

private const val Y_AXIS_LABEL_COUNT = 5
private const val Y_AXIS_GRANULARITY = 3f

fun BarChart.setup() {
    disableUnneededFeatures()
    setupOffsets()
    setupFonts()
    setupAxes()
    setupNoDataText()
}

private fun BarChart.setupOffsets() {
    setExtraOffsets(LEFT_OFFSET, TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET)
}

private fun BarChart.setupNoDataText() {
    val textSize = 14f.sp.toPx(context)
    getPaint(PAINT_INFO).textSize = textSize.toFloat()
    setNoDataTextColor(context.getTextColor())
    setNoDataText(context.getString(R.string.no_stats))
}

private fun BarChart.setupFonts() {
    val textSize = 12.5f.sp.toDp(context)
    axisLeft.textSize = textSize
    axisLeft.xOffset = 7f
    xAxis.textSize = textSize
}

private fun BarChart.setupAxes() {
    setupXAxis()
    setupLeftAxis()
}

private fun BarChart.setupXAxis() {
    xAxis.run {
        valueFormatter = WeekDayFormatter()
        spaceMin = X_AXIS_SPACE_MIN
        spaceMax = X_AXIS_SPACE_MAX
        yOffset = X_AXIS_Y_OFFSET
        textColor = context.getTextColor()
        position = BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(false)
    }
}

private fun BarChart.setupLeftAxis() {
    val textColor = context.getTextColor()

    axisLeft.run {
        setLabelCount(Y_AXIS_LABEL_COUNT, true)
        valueFormatter = TimeFormatter(context)
        isGranularityEnabled = true
        granularity = Y_AXIS_GRANULARITY
        this.textColor = textColor
        axisMinimum = 0f
        gridColor = ColorUtils.setAlphaComponent(textColor, 0x30)
        gridLineWidth = 1.5f
        setDrawAxisLine(false)
    }
}

private fun BarChart.disableUnneededFeatures() {
    legend.isEnabled = false
    axisRight.isEnabled = false
    axisLeft.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false
}
