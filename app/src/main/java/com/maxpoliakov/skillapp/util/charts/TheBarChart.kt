package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.util.ui.primaryColor
import com.maxpoliakov.skillapp.util.ui.sp
import com.maxpoliakov.skillapp.util.ui.textColor

class TheBarChart : BarChart {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private var intervalType: UiStatisticInterval? = null

    init {
        setup()
    }

    fun update(data: BarChartData?) {
        if (data != null) {
            updateUI(data)
        } else {
            this.data = null
        }

        notifyDataSetChanged()
        invalidate()
    }

    private fun updateUI(data: BarChartData) {
        showGoalIfNecessary(data)
        updateInterval(data)
        updateData(data)
    }

    private fun updateData(data: BarChartData) {
        this.data = createBarData(data)
        scrollToLast(data)
    }

    private fun createBarData(data: BarChartData): BarData {
        return BarData(createDataSets(data)).apply {
            barWidth = 0.3f
        }
    }

    private fun createDataSets(data: BarChartData): List<BarDataSet> {
        return listOf(createDataSet(data))
    }

    private fun createDataSet(data: BarChartData): BarDataSet {
        return BarDataSet(data.entries, "").apply {
            valueTextSize = VALUE_TEXT_SIZE
            valueFormatter = data.unit.getValueFormatter(context)
            valueTextColor = context.textColor
            color = context.primaryColor
            isHighlightEnabled = false
        }
    }

    private fun setup() {
        disableUnneededFeatures()
        setupOffsets()
        setupFonts()
        setupAxes()
        setupZoom()
        setupNoDataText()
    }

    private fun setupZoom() {
        viewPortHandler.setMaximumScaleY(1f)
        setXAxisRenderer(MultilineXAxisRenderer(this))
    }

    private fun setupOffsets() {
        setExtraOffsets(LEFT_OFFSET, TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET)
    }

    private fun setupNoDataText() {
        val textSize = 14f.sp.toPx(context)
        getPaint(PAINT_INFO).textSize = textSize.toFloat()
        setNoDataTextColor(context.textColor)
        setNoDataText(context.getString(R.string.no_stats))
    }

    private fun setupFonts() {
        val textSize = 12.5f.sp.toDp(context)
        axisLeft.textSize = textSize
        axisLeft.xOffset = 7f
        xAxis.textSize = textSize
    }

    private fun setupAxes() {
        setupXAxis()
        setupLeftAxis()
    }

    private fun setupXAxis() {
        xAxis.run {
            valueFormatter = DayFormatter()
            spaceMin = X_AXIS_SPACE_MIN
            spaceMax = X_AXIS_SPACE_MAX
            yOffset = X_AXIS_Y_OFFSET
            textColor = context.textColor
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }
    }

    private fun setupLeftAxis() {
        axisLeft.run {
            axisMinimum = 0f
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
        }
    }

    private fun disableUnneededFeatures() {
        legend.isEnabled = false
        axisRight.isEnabled = false
        description.isEnabled = false
    }

    private fun showGoalIfNecessary(data: BarChartData) {
        if (data.goal == null || !shouldDisplayGoal(data.goal)) {
            hideGoal()
        } else {
            showGoal(data.goal, data)
        }
    }

    private fun showGoal(goal: UiGoal, data: BarChartData) {
        showLimitLine(goal)
        updateAxisMaximum(data)
    }

    private fun showLimitLine(goal: UiGoal) = axisLeft.run {
        removeAllLimitLines()
        addLimitLine(makeLimitLine(goal))
    }

    private fun makeLimitLine(goal: UiGoal): LimitLine {
        return LimitLine(
            goal.count.toFloat(),
            goal.unit.toLongString(goal.count, context),
        ).apply {
            val color = context.textColor
            lineWidth = 1f
            lineColor = color
            textColor = color
            labelPosition = LimitLabelPosition.LEFT_BOTTOM
            textSize = 12.sp.toDp(context)
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
    }

    private fun hideGoal() {
        axisLeft.removeAllLimitLines()
        axisLeft.resetAxisMaximum()
    }

    private fun updateAxisMaximum(data: BarChartData) {
        if (data.goal == null) {
            axisLeft.resetAxisMaximum()
        } else {
            val axisMaximum = (data.entries.maxByOrNull { it.y }?.y ?: 0f) * 1.1f
            val goalTime = data.goal.count.toFloat()

            if (goalTime < axisMaximum) {
                axisLeft.resetAxisMaximum()
            } else {
                axisLeft.axisMaximum = data.goal.count.toFloat().coerceAtLeast(axisMaximum)
            }
        }
    }

    private fun shouldDisplayGoal(goal: UiGoal): Boolean {
        return goal.type.toDomain().interval.mapToUI() == intervalType
    }

    private fun updateInterval(data: BarChartData) {
        if (data.interval == this.intervalType) return

        intervalType = data.interval
        xAxis.valueFormatter = data.interval.valueFormatter

        viewPortHandler.setScaleXRange(data.interval.scale)
        setScaleEnabled(data.interval.scaleEnabled)
    }

    private fun scrollToLast(data: BarChartData) {
        zoom(data.interval.scale.endInclusive, 1f, data.entries.last().x, 0f)
    }

    companion object {
        private const val VALUE_TEXT_SIZE = 12f

        private const val LEFT_OFFSET = 15f
        private const val TOP_OFFSET = 27f
        private const val RIGHT_OFFSET = 20f
        private const val BOTTOM_OFFSET = 60f

        private const val X_AXIS_SPACE_MIN = 0.4f
        private const val X_AXIS_SPACE_MAX = 0.4f
        private const val X_AXIS_Y_OFFSET = 10f
    }
}
