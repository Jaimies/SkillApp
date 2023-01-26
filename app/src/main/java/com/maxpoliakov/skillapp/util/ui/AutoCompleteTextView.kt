package com.maxpoliakov.skillapp.util.ui

import android.R
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView

fun AutoCompleteTextView.setup(values: List<String>) {
    val adapter = NonFilteringArrayAdapter(context, R.layout.simple_dropdown_item_1line, values)
    setAdapter(adapter)
    setText(values[0], false)
    inputType = EditorInfo.TYPE_NULL
}
