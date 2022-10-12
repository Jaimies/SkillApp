package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.ui.common.MonthFormatter
import com.maxpoliakov.skillapp.ui.common.WeekFormatter

enum class UiStatisticInterval {
    Daily {
        override val valueFormatter get() = DayFormatter()
        override val scaleEnabled get() = true
        override val minScale get() = 4f
        override val maxScale get() = 8f

        override fun mapToDomain() = StatisticInterval.Daily
    },

    Weekly {
        override val valueFormatter get() = WeekFormatter()

        override fun mapToDomain() = StatisticInterval.Weekly
    },

    Monthly {
        override val valueFormatter get() = MonthFormatter()

        override fun mapToDomain() = StatisticInterval.Monthly
    },
    ;

    abstract val valueFormatter: ValueFormatter

    open val scaleEnabled = false
    open val minScale = 3f
    open val maxScale = 3f

    abstract fun mapToDomain(): StatisticInterval

    companion object {
        fun from(unit: StatisticInterval): UiStatisticInterval {
            return values().find { uiUnit -> uiUnit.mapToDomain() == unit }
                ?: throw IllegalArgumentException("Unknown measurement unit: $unit")
        }

        fun StatisticInterval.mapToUI() = from(this)
    }
}
