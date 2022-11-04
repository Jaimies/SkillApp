package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.ui.common.MonthFormatter
import com.maxpoliakov.skillapp.ui.common.WeekFormatter
import com.maxpoliakov.skillapp.ui.common.YearFormatter

enum class UiStatisticInterval : MappableEnum<UiStatisticInterval, StatisticInterval> {
    Daily {
        override val valueFormatter get() = DayFormatter()
        override val numberOfValuesVisibleAtOnce get() = 7..14

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

    Yearly {
        override val valueFormatter get() = YearFormatter()

        override fun toDomain() = StatisticInterval.Yearly
    }
    ;

    abstract val valueFormatter: ValueFormatter

    open val numberOfValuesVisibleAtOnce get() = 7..7

    val scale get() = scaleToDisplayNValues(numberOfValuesVisibleAtOnce)
    val scaleEnabled get() = scale.start != scale.endInclusive

    private fun scaleToDisplayNValues(n: Int): Float {
        return toDomain().numberOfValues / n.toFloat()
    }

    private fun scaleToDisplayNValues(n: IntRange): ClosedFloatingPointRange<Float> {
        return scaleToDisplayNValues(n.first)..scaleToDisplayNValues(n.last)
    }

    companion object : MappableEnum.Companion<UiStatisticInterval, StatisticInterval>(values())
}
