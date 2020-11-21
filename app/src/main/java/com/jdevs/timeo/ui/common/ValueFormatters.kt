package com.jdevs.timeo.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.shared.util.EPOCH
import com.jdevs.timeo.shared.util.shortName
import com.jdevs.timeo.util.time.toReadableFloat
import java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR
import kotlin.math.roundToInt

class WeekDayFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return EPOCH.plusDays(value.toLong()).dayOfWeek.shortName
    }
}

class YearWeekFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return EPOCH.plusWeeks(value.toLong()).get(WEEK_OF_WEEK_BASED_YEAR).toString()
    }
}

class YearMonthFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return EPOCH.plusMonths(value.toLong()).month.shortName
    }
}

class TimeFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value < 0) return ""
        if (value < 1) return "${(value * 60).roundToInt()}m"
        return "${value.toReadableFloat()}h"
    }
}
