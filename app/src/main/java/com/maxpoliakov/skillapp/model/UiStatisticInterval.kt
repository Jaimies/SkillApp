package com.maxpoliakov.skillapp.model

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.MappableEnum
import com.maxpoliakov.skillapp.shared.chart.valueformatter.DayFormatter
import com.maxpoliakov.skillapp.shared.chart.valueformatter.MonthFormatter
import com.maxpoliakov.skillapp.shared.chart.valueformatter.WeekFormatter
import com.maxpoliakov.skillapp.shared.chart.valueformatter.YearFormatter
import com.maxpoliakov.skillapp.shared.time.toReadableDate
import com.maxpoliakov.skillapp.shared.time.toShortReadableDate
import com.maxpoliakov.skillapp.shared.util.fullLocalizedName
import java.time.LocalDate

enum class UiStatisticInterval : MappableEnum<UiStatisticInterval, StatisticInterval> {
    Daily {
        override val valueFormatter get() = DayFormatter()

        override fun formatDateRange(range: ClosedRange<LocalDate>, context: Context): String {
            return context.toReadableDate(range.start)
        }

        override val numberOfValuesVisibleAtOnce get() = 7..14

        override fun toDomain() = StatisticInterval.Daily
    },

    Weekly {
        override val valueFormatter get() = WeekFormatter()

        override fun formatDateRange(range: ClosedRange<LocalDate>, context: Context): String {
            return context.getString(
                R.string.date_range,
                context.toShortReadableDate(range.start),
                context.toShortReadableDate(range.endInclusive),
            )
        }

        override fun toDomain() = StatisticInterval.Weekly
    },

    Monthly {
        override val valueFormatter get() = MonthFormatter()
        override fun formatDateRange(range: ClosedRange<LocalDate>, context: Context): String {
            return context.getString(
                R.string.date_month_and_year,
                range.start.month.fullLocalizedName,
                range.start.year,
            )
        }

        override fun toDomain() = StatisticInterval.Monthly
    },

    Yearly {
        override val valueFormatter get() = YearFormatter()

        override fun formatDateRange(range: ClosedRange<LocalDate>, context: Context): String {
            return context.getString(R.string.date_year_only, range.start.year)
        }

        override fun toDomain() = StatisticInterval.Yearly
    },
    ;

    abstract val valueFormatter: ValueFormatter

    // todo perhaps restructure UIStatisticInterval to make this method make more sense
    abstract fun formatDateRange(range: ClosedRange<LocalDate>, context: Context): String

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
