package com.jdevs.timeo.util.fragment

import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

inline fun Fragment.showTimePicker(crossinline onTimeSet: (hours: Int, minutes: Int) -> Unit) {
    val dialog = TimePickerDialog.newInstance({ _, hour, minute, _ ->
        if (hour != 0 || minute != 0) onTimeSet(hour, minute)
    }, 0, 0, true)

    dialog.show(childFragmentManager, null)
}
