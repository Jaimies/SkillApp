package com.maxpoliakov.skillapp.util.fragment

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import java.time.LocalTime

inline fun FragmentManager.showTimePicker(
    initialValue: LocalTime,
    crossinline onSelected: (LocalTime) -> Unit,
) {
    // todo set 24 hour mode if necessary
    val picker = MaterialTimePicker.Builder()
        .setHour(initialValue.hour)
        .setMinute(initialValue.minute)
        .build()

    picker.addOnPositiveButtonClickListener {
        val time = LocalTime.of(picker.hour, picker.minute)
        if (time != initialValue) onSelected(time)
    }

    picker.show(this, null)
}
