package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats

data class BarChartData(
    val interval: UiStatisticInterval,
    val entries: List<BarEntry>?,
) {
    companion object {
        fun from(interval: StatisticInterval, entries: List<Statistic>): BarChartData {
            return BarChartData(
                interval.mapToUI(),
                entries.withMissingStats(interval).toEntries(interval)
            )
        }
    }
}
