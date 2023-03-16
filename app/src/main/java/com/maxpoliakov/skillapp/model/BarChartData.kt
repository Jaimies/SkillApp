package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.chart.toEntries
import com.maxpoliakov.skillapp.shared.chart.withMissingStats
import java.time.LocalDate

data class BarChartData(
    val interval: UiStatisticInterval,
    val entries: List<BarEntry>,
    val unit: UiMeasurementUnit,
    val goal: UiGoal?,
) {
    val shouldDisplayGoal: Boolean
        get() {
            return goal != null && goal.type.toDomain().interval == interval.toDomain()
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
