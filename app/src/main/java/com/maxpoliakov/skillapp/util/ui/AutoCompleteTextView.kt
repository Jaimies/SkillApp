package com.maxpoliakov.skillapp.util.ui

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

fun AutoCompleteTextView.setup(values: List<String>) {
    val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, values)
    setAdapter(adapter)
    setText(values[0], false)
}
