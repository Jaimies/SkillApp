package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import com.maxpoliakov.skillapp.util.ui.textColor

class ThePieChart : PieChart, OnChartValueSelectedListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setup()
    }

    // todo split into different functions
    fun setup() {
        setHoleColor(Color.TRANSPARENT)
        holeRadius = 52.5f
        transparentCircleRadius = 0f
        description.isEnabled = false

        legend.run {
            textColor = context.getColorAttributeValue(android.R.attr.textColorPrimary)
            setDrawInside(false)
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.VERTICAL
        }

        extraBottomOffset = 5f
        // todo better formatting
        setDrawCenterText(true)
        setCenterTextSize(24f)
        setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        setCenterTextColor(context.textColor)
        setOnChartValueSelectedListener(this)
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
        if (entry is PieEntry) {
            centerText = entry.label
        }


    }

    override fun onNothingSelected() {
        centerText = ""
    }
}
