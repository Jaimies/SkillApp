package com.maxpoliakov.skillapp.util.charts

import android.graphics.Color
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

    extraBottomOffset = 5f
}

fun PieChart.setData(entries: List<PieEntry>) {
    val validEntries = entries
        .filter { it.y > 0 }
        .sortedBy { -it.y }
        .take(5)

    if (validEntries.isEmpty()) {
        this.data = null
        return
    }

    val dataSet = PieDataSet(validEntries, "").apply {
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

    data = PieData(dataSet)
    invalidate()
}
