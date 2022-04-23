package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

fun Duration?.format(context: Context): String {
    if (this == null) return ""

    val hours = toHours()
    val minutesPart = toMinutesPartCompat()

    return when {
        hours == 0L -> context.getString(R.string.time_minutes, minutesPart.toString())
        minutesPart == 0L -> context.getString(R.string.time_hours, hours.toString())
        else -> context.getString(R.string.time_hours_and_minutes, hours, minutesPart)
    }
}
