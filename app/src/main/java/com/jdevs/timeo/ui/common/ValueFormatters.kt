package com.jdevs.timeo.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.shared.time.EPOCH
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.IsoFields
import java.util.Locale

class WeekDayFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusDays(value.toLong())
            .dayOfWeek
            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .toUpperCase(Locale.getDefault())
    }
}

class YearWeekFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusWeeks(value.toLong()).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR).toString()
    }
}

class YearMonthFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusMonths(value.toLong())
            .month
            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .toUpperCase(Locale.getDefault())
    }
}
