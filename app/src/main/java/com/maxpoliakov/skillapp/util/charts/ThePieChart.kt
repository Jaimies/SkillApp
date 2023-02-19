package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
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
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import com.maxpoliakov.skillapp.util.ui.sp
import com.maxpoliakov.skillapp.util.ui.textColor

class ThePieChart : PieChart, OnChartValueSelectedListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private var selectionListener: OnChartValueSelectedListener? = null
    private var selectedEntry: ThePieEntry? = null

    init {
        setup()
    }

    private fun setup() {
        setupHole()
        setupLegend()
        setupCenterText()
        setupDescription()
        setupOffsets()
        setupNoDataText()
        setupListeners()
    }

    private fun setupListeners() {
        setOnChartValueSelectedListenerInternal(this)
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
    }

    private fun setupDescription() {
        description.isEnabled = false
    }

    private fun setupOffsets() {
        extraBottomOffset = 5f
    }

    private fun setupNoDataText() {
        val textSize = 14f.sp.toPx(context)
        getPaint(PAINT_INFO).textSize = textSize.toFloat()
        setNoDataTextColor(context.textColor)
        setNoDataText(context.getString(R.string.no_data_for_given_interval))
    }

    fun update(data: PieChartData) {
        if (data.entries.isEmpty()) {
            this.data = null
            invalidate()
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
        updateHighlight(data)
        invalidate()
    }

    private fun updateHighlight(newData: PieChartData) {
        val selectedEntry = selectedEntry ?: return

        if (newData.contains(selectedEntry)) {
            updateHighlight(newData, selectedEntry)
        } else {
            removeHighlight()
        }
    }

    private fun updateHighlight(newData: PieChartData, selectedEntry: ThePieEntry) {
        try {
            val indexOfEntry = newData.indexOfOrNull(selectedEntry) ?: return
            highlightValue(indexOfEntry.toFloat(), 0, true)
        } catch(e: Exception) {
        }
    }

    private fun PieChartData.indexOfOrNull(selectedEntry: ThePieEntry): Int? {
        return entries
            .indexOfFirst { entry -> entry.skillId == selectedEntry.skillId }
            .takeUnless { it < 0 }
    }

    private fun PieChartData.contains(selectedEntry: ThePieEntry): Boolean {
        return entries.any { entry -> entry.skillId == selectedEntry.skillId }
    }

    private fun removeHighlight() {
        highlightValue(null, true)
    }

    // Chart<*> only supports setting a single OnChartValueSelectedListener.
    // Since we are already using one within ThePieChart, we need to make it
    // possible to set an OnChartValueSelectedListener from outside of ThePieChart
    // without breaking its functionality.
    override fun setOnChartValueSelectedListener(listener: OnChartValueSelectedListener?) {
        this.selectionListener = listener
    }

    private fun setOnChartValueSelectedListenerInternal(listener: OnChartValueSelectedListener?) {
        super.setOnChartValueSelectedListener(listener)
    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        if (entry is ThePieEntry) {
            selectedEntry = entry
            updateCenterText(entry)
        } else {
            selectedEntry = null
        }

        selectionListener?.onValueSelected(entry, highlight)
    }

    private fun updateCenterText(entry: ThePieEntry) {
        centerText = buildSpannedString {
            addLine(entry.name, StyleSpan(Typeface.BOLD), RelativeSizeSpan(1.5f))
            append("\n")
            addLine(entry.formattedValue)
        }
    }

    private fun SpannableStringBuilder.addLine(text: CharSequence, vararg spans: Any): SpannableStringBuilder {
        val start = length

        append("\u00A0")
        append(text)
        append("\u00A0")

        for (span in spans.toList() + EllipsizeLineSpan()) {
            setSpan(span, start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        return this
    }

    override fun onNothingSelected() {
        selectedEntry = null
        centerText = ""
        selectionListener?.onNothingSelected()
    }
}
