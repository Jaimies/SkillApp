package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.ui.common.MonthFormatter
import com.maxpoliakov.skillapp.ui.common.WeekFormatter

enum class UiStatisticInterval : MappableEnum<UiStatisticInterval, StatisticInterval> {
    Daily {
        override val valueFormatter get() = DayFormatter()
        override val scaleEnabled get() = true
        override val minScale get() = 4f
        override val maxScale get() = 8f

        override fun toDomain() = StatisticInterval.Daily
    },

    Weekly {
        override val valueFormatter get() = WeekFormatter()

        override fun toDomain() = StatisticInterval.Weekly
    },

    Monthly {
        override val valueFormatter get() = MonthFormatter()

        override fun toDomain() = StatisticInterval.Monthly
    },
    ;

    abstract val valueFormatter: ValueFormatter

    open val scaleEnabled = false
    open val minScale = 3f
    open val maxScale = 3f

    companion object : MappableEnum.Companion<UiStatisticInterval, StatisticInterval>(values())
}
