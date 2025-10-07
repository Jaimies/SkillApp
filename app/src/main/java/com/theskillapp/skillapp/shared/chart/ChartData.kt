package com.theskillapp.skillapp.shared.chart

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.highlight.Highlight
import com.theskillapp.skillapp.model.BarChartData
import com.theskillapp.skillapp.model.PieChartData
import com.theskillapp.skillapp.model.UiStatisticInterval
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

interface ChartData {
    val barChartData: LiveData<BarChartData?>
    val pieChartData: LiveData<PieChartData?>
    val interval: LiveData<UiStatisticInterval>
    val selectedDateRange: LiveData<ClosedRange<LocalDate>?>

    val barChartHighlight: MutableStateFlow<Highlight?>
    val pieChartHighlight: MutableStateFlow<Highlight?>

    fun setInterval(interval: UiStatisticInterval)
}
