package com.maxpoliakov.skillapp.util.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.maxpoliakov.skillapp.data.persistence.getIntPreference
import com.maxpoliakov.skillapp.data.persistence.saveIntPreference
import com.maxpoliakov.skillapp.data.persistence.sharedPrefs
import com.maxpoliakov.skillapp.shared.util.durationOfHoursAndMinutes
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.Duration

inline fun Context.showTimePicker(
    initialTime: Duration = Duration.ZERO,
    fragmentManager: FragmentManager = getFragmentManager(),
    crossinline onTimeSet: (Duration) -> Unit
) {
    val dialog = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(initialTime.toHours().toInt())
        .setMinute(initialTime.toMinutes().toInt())
        .setInputMode(sharedPrefs.getIntPreference(INPUT_MODE, INPUT_MODE_CLOCK))
        .build()

    dialog.addOnPositiveButtonClickListener {
        sharedPrefs.saveIntPreference(INPUT_MODE, dialog.inputMode)
        val time = durationOfHoursAndMinutes(dialog.hour, dialog.minute)
        if (time > Duration.ZERO)
            onTimeSet(time)
    }

    dialog.show(fragmentManager, null)
}

inline fun Fragment.showTimePicker(crossinline onTimeSet: (Duration) -> Unit) {
    requireContext().showTimePicker(
        fragmentManager = childFragmentManager,
        onTimeSet = onTimeSet
    )
}

const val INPUT_MODE = "input_mode"
