package com.maxpoliakov.skillapp.util.charts

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ChartData {
    val stats: LiveData<BarChartData?>
    val pieData: LiveData<PieChartData?>
    val interval: LiveData<UiStatisticInterval>
    val selectedDateRange: LiveData<ClosedRange<LocalDate>?>

    fun setSelectedEntry(entry: Entry?)
    fun setStatisticType(type: StatisticInterval)
    fun setStatisticType(type: UiStatisticInterval)
    fun getChartData(interval: StatisticInterval): Flow<List<Statistic>>
}
