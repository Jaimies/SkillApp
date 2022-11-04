package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats

data class BarChartData(
    val interval: UiStatisticInterval,
    val entries: List<BarEntry>,
    val unit: UiMeasurementUnit,
    val goal: UiGoal?
) {
    companion object {
        fun from(
            interval: StatisticInterval,
            entries: List<Statistic>,
            unit: MeasurementUnit,
            goal: Goal?,
        ): BarChartData? {
            val mappedEntries = entries
                .withMissingStats(interval)
                .toEntries(interval)
                ?: return null

            return BarChartData(
                interval.mapToUI(),
                mappedEntries,
                unit.mapToUI(),
                goal?.mapToUI(unit),
            )
        }
    }
}
