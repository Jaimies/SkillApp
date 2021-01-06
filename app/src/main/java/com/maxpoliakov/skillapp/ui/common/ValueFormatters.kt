package com.maxpoliakov.skillapp.ui.common

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.shortName
import com.maxpoliakov.skillapp.util.time.toReadableFloat

class WeekDayFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return EPOCH.plusDays(value.toLong()).dayOfWeek.shortName
    }
}

class TimeFormatter(private val context: Context) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value < 60) return ""
        if (value >= 3600) return toHours(value)
        return toMinutes(value)
    }

    private fun toMinutes(value: Float): String {
        return context.getString(R.string.minutes, (value / 60).toReadableFloat())
    }

    private fun toHours(value: Float): String {
        return context.getString(R.string.hours, (value / 3600).toReadableFloat())
    }
}
