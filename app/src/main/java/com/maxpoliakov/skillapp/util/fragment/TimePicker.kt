package com.maxpoliakov.skillapp.util.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.util.ui.DurationPicker
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.Duration

inline fun Context.showTimePicker(
    initialTime: Duration = Duration.ZERO,
    fragmentManager: FragmentManager = getFragmentManager(),
    allowZeroTime: Boolean = false,
    crossinline onTimeSet: (Duration) -> Unit
) {
    val dialog = DurationPicker.Builder()
        .setDuration(initialTime)
        .build()

    dialog.addOnPositiveButtonClickListener(View.OnClickListener {
        val time = dialog.duration
        if (time > Duration.ZERO || allowZeroTime)
            onTimeSet(time)
    })

    dialog.show(fragmentManager, null)
}

inline fun Fragment.showTimePicker(crossinline onTimeSet: (Duration) -> Unit) {
    requireContext().showTimePicker(
        fragmentManager = childFragmentManager,
        onTimeSet = onTimeSet
    )
}
