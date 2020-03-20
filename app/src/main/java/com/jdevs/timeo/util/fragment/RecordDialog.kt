package com.jdevs.timeo.util.fragment

import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

inline fun Fragment.showRecordDialog(crossinline onTimeSet: (minute: Int, hour: Int) -> Unit) {

    val onTimeSetListener = { _: TimePickerDialog, hour: Int, minute: Int, _: Int ->

        if (hour != 0 || minute != 0) {
            onTimeSet(hour, minute)
        }
    }

    val dialog = TimePickerDialog.newInstance(onTimeSetListener, 0, 0, true)
    dialog.show(childFragmentManager, null)
}
