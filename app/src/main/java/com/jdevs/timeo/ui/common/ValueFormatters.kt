package com.jdevs.timeo.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.shared.util.EPOCH
import com.jdevs.timeo.shared.util.HOUR_MINUTES
import com.jdevs.timeo.shared.util.shortName
import com.jdevs.timeo.util.time.toReadableFloat
import org.threeten.bp.temporal.IsoFields
import kotlin.math.roundToInt

class WeekDayFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusDays(value.toLong()).dayOfWeek.shortName
    }
}

class YearWeekFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusWeeks(value.toLong()).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR).toString()
    }
}

class YearMonthFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusMonths(value.toLong()).month.shortName
    }
}

class TimeFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float) = when {
        value <= 0 -> ""
        value < 1 -> "${(value * HOUR_MINUTES).roundToInt()}m"
        else -> "${value.toReadableFloat()}h"
    }
}
