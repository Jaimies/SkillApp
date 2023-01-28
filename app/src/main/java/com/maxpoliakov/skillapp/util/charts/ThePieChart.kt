package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import androidx.core.text.buildSpannedString
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import com.maxpoliakov.skillapp.util.ui.setSpanForWholeString
import com.maxpoliakov.skillapp.util.ui.sp
import com.maxpoliakov.skillapp.util.ui.textColor

class ThePieChart : PieChart, OnChartValueSelectedListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setup()
    }

    private fun setup() {
        setupHole()
        setupLegend()
        setupCenterText()
        setupDescription()
        setupOffsets()
    }

    private fun setupHole() {
        setHoleColor(Color.TRANSPARENT)
        holeRadius = 52.5f
        transparentCircleRadius = 0f
    }

    private fun setupLegend() = legend.run {
        textColor = context.getColorAttributeValue(android.R.attr.textColorPrimary)
        setDrawInside(false)
        textSize = 10.sp.toDp(context)
        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        orientation = Legend.LegendOrientation.VERTICAL
    }

    private fun setupCenterText() {
        setDrawCenterText(true)
        setCenterTextSize(16.sp.toDp(context))
        setCenterTextColor(context.textColor)
        setOnChartValueSelectedListener(this)
    }

    private fun setupDescription() {
        description.isEnabled = false
    }

    private fun setupOffsets() {
        extraBottomOffset = 5f
    }

    fun update(data: PieChartData) {
        if (data.entries.isEmpty()) {
            this.data = null
            return
        }

        val dataSet = PieDataSet(data.entries, "").apply {
            val colors = intArrayOf(
                ColorTemplate.rgb("#2ecc71"),
                ColorTemplate.rgb("#ffc41e"),
                ColorTemplate.rgb("#e74c3c"),
                ColorTemplate.rgb("#3498db"),
                ColorTemplate.rgb("#9b49b6"),
            )

            setColors(colors, 255)
            setDrawEntryLabels(false)
            setDrawValues(false)
        }

        this.data = PieData(dataSet)
        invalidate()
    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        if (entry is ThePieEntry) {
            updateCenterText(entry)
        }
    }

    private fun updateCenterText(entry: ThePieEntry) {
        centerText = buildSpannedString {
            append("\u00A0")
            append(entry.name)
            append("\u00A0")
            setSpanForWholeString(StyleSpan(Typeface.BOLD))
            setSpanForWholeString(RelativeSizeSpan(1.5f))
            setSpanForWholeString(EllipsizeLineSpan())
            append("\n")
            append("\u00A0${entry.formattedValue}\u00A0", EllipsizeLineSpan(), 0)
        }
    }

    override fun onNothingSelected() {
        centerText = ""
    }
}
