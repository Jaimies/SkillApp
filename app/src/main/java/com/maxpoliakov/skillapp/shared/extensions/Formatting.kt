package com.maxpoliakov.skillapp.shared.extensions

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

@JvmOverloads
fun Duration?.format(context: Context, timeHoursAndMinutesResId: Int = R.string.time_hours_and_minutes): String {
    if (this == null) return ""

    val hours = toHours()
    val minutesPart = toMinutesPartCompat()

    return when {
        hours == 0L -> context.getString(R.string.time_minutes, minutesPart.toString())
        minutesPart == 0L -> context.getString(R.string.time_hours, hours.toString())
        else -> context.getString(timeHoursAndMinutesResId, hours, minutesPart)
    }
}

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}
