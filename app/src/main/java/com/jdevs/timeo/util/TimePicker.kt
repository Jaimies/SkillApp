package com.jdevs.timeo.util

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

fun Fragment.showTimePicker(onTimeSet: (minute: Int, hour: Int) -> Unit) {

    val dialog =
        TimePickerDialog.newInstance({ _, hour, minute, _ -> onTimeSet(hour, minute) }, 0, 0, true)

    dialog.setOkColor(Color.WHITE)
    dialog.setCancelColor(Color.WHITE)

    dialog.show(childFragmentManager, null)
}
