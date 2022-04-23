package com.maxpoliakov.skillapp.ui.common

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.shortName
import com.maxpoliakov.skillapp.util.time.toReadableFloat
import kotlin.math.roundToInt

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
        return "\n${date.month.shortName}"
    }
}

class TimeFormatter(private val context: Context) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value == 0f) return ""
        if (value < 60) return context.getString(R.string.time_minutes, "1")
        if (value >= 3600) return toHours(value)
        return toMinutes(value)
    }

    private fun toMinutes(value: Float): String {
        return context.getString(R.string.time_minutes, (value / 60).roundToInt().toString())
    }

    private fun toHours(value: Float): String {
        return context.getString(R.string.time_hours, (value / 3600).toReadableFloat())
    }
}
