package com.maxpoliakov.skillapp.ui.common

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.shortName
import com.maxpoliakov.skillapp.util.time.toReadableFloat
import java.time.Duration

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
        val duration = Duration.ofMillis(value.toLong())

        if (value == 0f) return ""
        if (duration < Duration.ofMinutes(1)) return context.getString(R.string.time_minutes, "1")
        if (duration >= Duration.ofHours(1)) return toHours(duration)
        return toMinutes(duration)
    }

    private fun toMinutes(duration: Duration): String {
        return context.getString(R.string.time_minutes, duration.toMinutes().toString())
    }

    private fun toHours(duration: Duration): String {
        return context.getString(R.string.time_hours, (duration.toMinutes() / 60f).toReadableFloat())
    }
}
