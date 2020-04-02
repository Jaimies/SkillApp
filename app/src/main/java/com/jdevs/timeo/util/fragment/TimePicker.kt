package com.jdevs.timeo.util.fragment

import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

inline fun Fragment.showTimePicker(crossinline onTimeSet: (hours: Int, minutes: Int) -> Unit) {

    val onTimeSetListener = { _: TimePickerDialog, hour: Int, minute: Int, _: Int ->

        if (hour != 0 || minute != 0) {
            onTimeSet(hour, minute)
        }
    }

    val dialog = TimePickerDialog.newInstance(onTimeSetListener, 0, 0, true)
    dialog.show(childFragmentManager, null)
}
