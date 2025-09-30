package com.theskillapp.skillapp.shared.fragment

import android.content.Context
import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker.InputMode
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.theskillapp.skillapp.data.extensions.sharedPrefs
import java.time.LocalTime

fun FragmentManager.showTimePicker(
    context: Context,
    @StringRes titleResId: Int,
    initialValue: LocalTime,
    onSelected: (LocalTime) -> Unit,
) {
    val picker = MaterialTimePicker.Builder()
        .setHour(initialValue.hour)
        .setMinute(initialValue.minute)
        .setTitleText(titleResId)
        .setTimeFormat(getPreferredTimeFormat(context))
        .setInputMode(getPreferredInputMode(context))
        .build()

    picker.addOnPositiveButtonClickListener {
        val time = LocalTime.of(picker.hour, picker.minute)
        if (time != initialValue) onSelected(time)
        setPreferredInputMode(context, picker.inputMode)
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

@InputMode
private fun getPreferredInputMode(context: Context): Int {
    return context.sharedPrefs.getInt(PREFERRED_INPUT_MODE, INPUT_MODE_CLOCK)
}

private fun setPreferredInputMode(context: Context, @InputMode inputMode: Int) {
    context.sharedPrefs.edit { putInt(PREFERRED_INPUT_MODE, inputMode) }
}

private const val PREFERRED_INPUT_MODE = "TimePicker.PREFERRED_INPUT_MODE"
