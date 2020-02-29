package com.jdevs.timeo.util

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

fun Fragment.showTimePicker(onTimeSet: (minute: Int, hour: Int) -> Unit) {

    val onTimeSetListener = { _: TimePickerDialog, hour: Int, minute: Int, _: Int ->

        if (hour != 0 && minute != 0) {
            onTimeSet(hour, minute)
        }
    }

    val dialog = TimePickerDialog.newInstance(onTimeSetListener, 0, 0, true)

    dialog.setOkColor(Color.WHITE)
    dialog.setCancelColor(Color.WHITE)

    dialog.show(childFragmentManager, null)
}
