package com.maxpoliakov.skillapp.util.fragment

import android.content.Context
import com.google.android.material.datepicker.MaterialDatePicker
import com.maxpoliakov.skillapp.shared.util.dateOfEpochMillis
import com.maxpoliakov.skillapp.shared.util.millisSinceEpoch
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import java.time.LocalDate

inline fun Context.showDatePicker(
    selectedDate: LocalDate,
    crossinline onSelected: (LocalDate) -> Unit
) {
    val picker = MaterialDatePicker.Builder.datePicker()
        .setSelection(selectedDate.millisSinceEpoch)
        .build()

    picker.addOnPositiveButtonClickListener { millis ->
        val date = dateOfEpochMillis(millis)
        if (date != selectedDate) onSelected(date)
    }

    picker.show(getFragmentManager(), null)
}
