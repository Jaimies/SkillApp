package com.maxpoliakov.skillapp.shared.chart

import android.content.Context
import android.graphics.Typeface
import android.os.Parcelable
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.jobs.MoveViewJob
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.shared.Dimension.Companion.sp
import com.maxpoliakov.skillapp.shared.chart.valueformatter.DateRangeValueFormatter
import com.maxpoliakov.skillapp.shared.extensions.primaryColor
import com.maxpoliakov.skillapp.shared.extensions.textColor
import kotlinx.parcelize.Parcelize

class TheBarChart : BarChart, TheChart<BarChartData> {
    private var stateRestored = false

    init {
        setup()
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onSaveInstanceState(): Parcelable {
        return PersistedState(lowestVisibleX, scaleX, super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is PersistedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superSavedState)

        zoom(state.scaleX, scaleY, state.lowestVisibleX, 0f)
        moveViewToX(state.lowestVisibleX)

        stateRestored = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        // fix for https://github.com/PhilJay/MPAndroidChart/issues/2238
        MoveViewJob.getInstance(null, 0f, 0f, null, null)
    }

    override fun update(data: BarChartData?) {
        if (data != null) {
            updateUI(data)
        } else {
            this.data = null
        }

        highlightValue(null)
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
        if (!stateRestored) scrollToLast(data)
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
            valueTextSize = 12.sp.toDp(context)
            valueFormatter = data.unit.getValueFormatter(context)
            valueTextColor = context.textColor
            color = context.primaryColor
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
            spaceMin = X_AXIS_SPACE_MIN
            spaceMax = X_AXIS_SPACE_MAX
            yOffset = X_AXIS_Y_OFFSET
            textColor = context.textColor
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true
            granularity = 1f
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
        if (data.shouldDisplayGoal) showGoal(data)
        else hideGoal()
    }

    private fun showGoal(data: BarChartData) {
        updateAxisMaximum(data)
        showLimitLine(data.goal!!)
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
            labelPosition = getLimitLabelPosition(goal)
            textSize = 12.sp.toDp(context)
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
    }

    private fun getLimitLabelPosition(goal: UiGoal): LimitLabelPosition {
        if (goal.count / axisLeft.axisMaximum > 0.5) return LimitLabelPosition.LEFT_BOTTOM
        return LimitLabelPosition.LEFT_TOP
    }

    private fun hideGoal() {
        axisLeft.removeAllLimitLines()
        axisLeft.resetAxisMaximum()
    }

    private fun updateAxisMaximum(data: BarChartData) {
        val goalCount = data.goal?.count?.toFloat() ?: 0f
        axisLeft.axisMaximum = (data.getHighestYValue() * 1.1f).coerceAtLeast(goalCount)
    }

    private fun updateInterval(data: BarChartData) {
        xAxis.valueFormatter = DateRangeValueFormatter(data.interval)
        // todo too concrete
        viewPortHandler.setScaleXRange(
            if (data.entries.size < 7) 1f..1f
            else data.interval.scale
        )
        setScaleEnabled(data.interval.scaleEnabled)
    }

    private fun scrollToLast(data: BarChartData) {
        zoom(data.interval.scale.endInclusive, 1f, data.entries.last().x, 0f)
        moveViewToX(data.entries.last().x)
    }

    @Parcelize
    private data class PersistedState(
        val lowestVisibleX: Float,
        val scaleX: Float,
        val superSavedState: Parcelable?,
    ) : BaseSavedState(superSavedState), Parcelable

    companion object {
        private const val LEFT_OFFSET = 15f
        private const val TOP_OFFSET = 27f
        private const val RIGHT_OFFSET = 20f
        private const val BOTTOM_OFFSET = 60f

        private const val X_AXIS_SPACE_MIN = 0.4f
        private const val X_AXIS_SPACE_MAX = 0.4f
        private const val X_AXIS_Y_OFFSET = 10f
    }
}
