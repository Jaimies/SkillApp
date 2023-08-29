package com.maxpoliakov.skillapp.shared.chart.valueformatter

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.extensions.toReadableFloat
import java.time.Duration

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
