package com.maxpoliakov.skillapp.util.charts

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.highlight.Highlight
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

interface ChartData {
    val barChartData: LiveData<BarChartData?>
    val pieChartData: LiveData<PieChartData?>
    val interval: LiveData<UiStatisticInterval>
    val selectedDateRange: LiveData<ClosedRange<LocalDate>?>

    val highlight: MutableStateFlow<Highlight?>

    fun setStatisticType(type: UiStatisticInterval)
    fun getChartData(interval: StatisticInterval): Flow<List<Statistic>>
}
