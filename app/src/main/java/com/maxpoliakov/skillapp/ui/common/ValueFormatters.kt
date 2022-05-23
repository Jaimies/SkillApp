package com.maxpoliakov.skillapp.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.shortName

class DayFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.plusDays(value.toLong())
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }
}

class WeekFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.atStartOfWeek().plusWeeks(value.toLong())
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }
}

class MonthFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = EPOCH.plusMonths(value.toLong())
        return "${date.month.shortName}\n${date.year % 100}"
    }
}
