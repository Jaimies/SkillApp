package com.maxpoliakov.skillapp.util.charts

import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import kotlinx.coroutines.flow.Flow

interface ChartData {
    val stats: LiveData<BarChartData?>

    // todo should be null if empty
    val pieData: LiveData<PieChartData>

    fun setSelectedEntry(entry: Entry?)
    fun setStatisticType(type: StatisticInterval)
    fun setStatisticType(type: UiStatisticInterval)
    fun getChartData(interval: StatisticInterval): Flow<List<Statistic>>
}
