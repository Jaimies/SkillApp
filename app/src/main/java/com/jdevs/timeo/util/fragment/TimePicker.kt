package com.jdevs.timeo.util.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Duration

inline fun Fragment.showTimePicker(crossinline onTimeSet: (Duration) -> Unit) {
    val dialog = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build()


    dialog.addOnPositiveButtonClickListener {
        val hours = Duration.ofHours(dialog.hour.toLong())
        val minutes = Duration.ofMinutes(dialog.minute.toLong())
        onTimeSet(hours.plus(minutes))
    }

    dialog.show(childFragmentManager, null)
}
