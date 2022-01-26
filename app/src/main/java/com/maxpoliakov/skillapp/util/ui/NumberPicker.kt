package com.maxpoliakov.skillapp.util.ui

import android.widget.EditText
import android.widget.NumberPicker

fun NumberPicker.forceFirstValueToFormat() {
    val editView = getChildAt(0)

    if (editView is EditText) {
        editView.filters = arrayOfNulls(0)
    }
}
