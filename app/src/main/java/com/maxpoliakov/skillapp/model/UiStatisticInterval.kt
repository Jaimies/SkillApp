package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.model.formatter.daterange.DayFormatter
import com.maxpoliakov.skillapp.model.formatter.daterange.MonthFormatter
import com.maxpoliakov.skillapp.model.formatter.daterange.DateRangeFormatter
import com.maxpoliakov.skillapp.model.formatter.daterange.WeekFormatter
import com.maxpoliakov.skillapp.model.formatter.daterange.YearFormatter
import com.maxpoliakov.skillapp.shared.MappableEnum

enum class UiStatisticInterval : MappableEnum<UiStatisticInterval, StatisticInterval> {
    Daily {
        override val formatter get() = DayFormatter()
        override val numberOfValuesVisibleAtOnce get() = 7..14

        override fun toDomain() = StatisticInterval.Daily
    },

    Weekly {
        override val formatter get() = WeekFormatter()
        override fun toDomain() = StatisticInterval.Weekly
    },

    Monthly {
        override val formatter get() = MonthFormatter()
        override fun toDomain() = StatisticInterval.Monthly
    },

    Yearly {
        override val formatter get() = YearFormatter()
        override fun toDomain() = StatisticInterval.Yearly
    },
    ;

    abstract val formatter: DateRangeFormatter

    open val numberOfValuesVisibleAtOnce get() = 7..7

    val scale get() = scaleToDisplayNValues(numberOfValuesVisibleAtOnce)
    val scaleEnabled get() = scale.start != scale.endInclusive

    private fun scaleToDisplayNValues(n: Int): Float {
        return toDomain().numberOfValues / n.toFloat()
    }

    private fun scaleToDisplayNValues(n: IntRange): ClosedFloatingPointRange<Float> {
        return scaleToDisplayNValues(n.last)..scaleToDisplayNValues(n.first)
    }

    companion object : MappableEnum.Companion<UiStatisticInterval, StatisticInterval>(values())
}
