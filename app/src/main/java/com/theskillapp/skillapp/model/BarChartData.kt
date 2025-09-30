package com.theskillapp.skillapp.model

import com.github.mikephil.charting.data.BarEntry
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Statistic
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.model.UiGoal.Companion.mapToUI
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.theskillapp.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.theskillapp.skillapp.shared.chart.toEntries
import com.theskillapp.skillapp.shared.chart.withMissingStats
import java.time.LocalDate

data class BarChartData(
    val interval: UiStatisticInterval,
    val entries: List<BarEntry>,
    val unit: UiMeasurementUnit,
    val goal: UiGoal?,
) {
    val shouldDisplayGoal: Boolean
        get() {
            return goal != null && goal.type.interval == interval.domainCounterpart
        }

    fun getHighestYValue(): Float {
        return entries.maxByOrNull { it.y }?.y ?: 0f
    }

    companion object {
        fun from(
            interval: StatisticInterval,
            entries: List<Statistic>,
            unit: MeasurementUnit<*>,
            goal: Goal?,
            dates: ClosedRange<LocalDate>,
        ): BarChartData? {
            val mappedEntries = entries
                .withMissingStats(interval, dates)
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
