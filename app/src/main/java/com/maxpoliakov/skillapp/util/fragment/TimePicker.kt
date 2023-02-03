package com.maxpoliakov.skillapp.util.fragment

import android.content.Context
import android.text.format.DateFormat
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.time.LocalTime

inline fun FragmentManager.showTimePicker(
    context: Context,
    initialValue: LocalTime,
    crossinline onSelected: (LocalTime) -> Unit,
) {
    val picker = MaterialTimePicker.Builder()
        .setHour(initialValue.hour)
        .setMinute(initialValue.minute)
        .setTimeFormat(getPreferredTimeFormat(context))
        .build()

    picker.addOnPositiveButtonClickListener {
        val time = LocalTime.of(picker.hour, picker.minute)
        if (time != initialValue) onSelected(time)
    }

    picker.show(this, null)
}

@TimeFormat
fun getPreferredTimeFormat(context: Context): Int {
    if (DateFormat.is24HourFormat(context)) {
        return CLOCK_24H
    }

    return CLOCK_12H
}
