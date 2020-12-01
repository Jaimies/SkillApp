package com.maxpoliakov.skillapp.util.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.maxpoliakov.skillapp.util.persistence.getIntPreference
import com.maxpoliakov.skillapp.util.persistence.saveIntPreference
import java.time.Duration

inline fun Fragment.showTimePicker(crossinline onTimeSet: (Duration) -> Unit) {
    val dialog = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setInputMode(requireContext().getIntPreference(INPUT_MODE, INPUT_MODE_CALENDAR))
        .build()


    dialog.addOnPositiveButtonClickListener {
        requireContext().saveIntPreference(INPUT_MODE, dialog.inputMode)
        val hours = Duration.ofHours(dialog.hour.toLong())
        val minutes = Duration.ofMinutes(dialog.minute.toLong())
        val time = hours.plus(minutes)
        if (time > Duration.ZERO)
            onTimeSet(time)
    }

    dialog.show(childFragmentManager, null)
}

const val INPUT_MODE = "input_mode"
