package com.maxpoliakov.skillapp.shared.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.shared.chart.TheBarChart
import com.maxpoliakov.skillapp.shared.chart.ThePieChart

@BindingAdapter("data", "highlight")
fun ThePieChart.setDataAndHighlight(data: PieChartData?, highlight: Highlight?) {
    update(data)
    if (data != null) highlightValue(highlight, true)
}

@BindingAdapter("data", "highlight")
fun TheBarChart.setDataAndHighlight(data: BarChartData?, highlight: Highlight?) {
    update(data)
    if (data != null) highlightValue(highlight, true)
}

@InverseBindingAdapter(attribute = "highlight", event = "onChartValueSelected")
fun Chart<*>.getHighlight(): Highlight? {
    return highlighted?.getOrNull(0)
}

@BindingAdapter("onChartValueSelected")
fun Chart<*>.setOnValueSelectedLister(onChanged: InverseBindingListener) {
    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) = onChanged.onChange()
        override fun onNothingSelected() = onChanged.onChange()
    })
}
