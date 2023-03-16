package com.maxpoliakov.skillapp.shared.fragment

import android.content.Context
import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.time.LocalTime

inline fun FragmentManager.showTimePicker(
    context: Context,
    @StringRes titleResId: Int,
    initialValue: LocalTime,
    crossinline onSelected: (LocalTime) -> Unit,
) {
    val picker = MaterialTimePicker.Builder()
        .setHour(initialValue.hour)
        .setMinute(initialValue.minute)
        .setTitleText(titleResId)
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
