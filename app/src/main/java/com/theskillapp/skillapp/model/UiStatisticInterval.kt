package com.theskillapp.skillapp.model

import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.model.formatter.daterange.DayFormatter
import com.theskillapp.skillapp.model.formatter.daterange.MonthFormatter
import com.theskillapp.skillapp.model.formatter.daterange.DateRangeFormatter
import com.theskillapp.skillapp.model.formatter.daterange.WeekFormatter
import com.theskillapp.skillapp.model.formatter.daterange.YearFormatter
import com.theskillapp.skillapp.shared.MappableEnum

enum class UiStatisticInterval(override val domainCounterpart: StatisticInterval) : MappableEnum<UiStatisticInterval, StatisticInterval> {
    Daily(StatisticInterval.Daily) {
        override val formatter get() = DayFormatter
        override val numberOfValuesVisibleAtOnce get() = 7..14
    },

    Weekly(StatisticInterval.Weekly) {
        override val formatter get() = WeekFormatter
    },

    Monthly(StatisticInterval.Monthly) {
        override val formatter get() = MonthFormatter
    },

    Yearly(StatisticInterval.Yearly) {
        override val formatter get() = YearFormatter
    },
    ;

    abstract val formatter: DateRangeFormatter

    open val numberOfValuesVisibleAtOnce get() = 7..7

    val scale get() = scaleToDisplayNValues(numberOfValuesVisibleAtOnce)
    val scaleEnabled get() = scale.start != scale.endInclusive

    private fun scaleToDisplayNValues(n: Int): Float {
        return domainCounterpart.numberOfValues / n.toFloat()
    }

    private fun scaleToDisplayNValues(n: IntRange): ClosedFloatingPointRange<Float> {
        return scaleToDisplayNValues(n.last)..scaleToDisplayNValues(n.first)
    }

    companion object : MappableEnum.Companion<UiStatisticInterval, StatisticInterval>(values())
}
