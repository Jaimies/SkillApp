package com.maxpoliakov.skillapp.util.charts

import android.graphics.Color
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue

fun PieChart.setup() {
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

    setExtraOffsets(0f, 0f, 0f, 20f)
}

fun PieChart.setData(entries: List<PieEntry>) {
    val validEntries = entries.filter { it.y > 0 }

    this.isVisible = validEntries.isNotEmpty()

    val dataSet = PieDataSet(validEntries, "").apply {
        setColors(ColorTemplate.COLORFUL_COLORS + ColorTemplate.MATERIAL_COLORS, 255)
        sliceSpace = 15f
        setDrawEntryLabels(false)
        setDrawValues(false)
    }

    data = PieData(dataSet)
    invalidate()
}
