package com.jdevs.timeo.util

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog

fun Fragment.showTimePicker(onTimeSet: (minute: Int, hour: Int) -> Unit) {

    val dialog =
        TimePickerDialog.newInstance({ _, hour, minute, _ -> onTimeSet(hour, minute) }, 0, 0, true)

    dialog.setOkColor(ContextCompat.getColor(requireContext(), android.R.color.white))
    dialog.setCancelColor(ContextCompat.getColor(requireContext(), android.R.color.white))

    dialog.show(childFragmentManager, null)
}
