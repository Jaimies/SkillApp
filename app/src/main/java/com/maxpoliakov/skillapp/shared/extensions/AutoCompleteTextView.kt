package com.maxpoliakov.skillapp.shared.extensions

import android.R
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import com.maxpoliakov.skillapp.shared.NonFilteringArrayAdapter

fun AutoCompleteTextView.setup(values: List<String>) {
    val adapter = NonFilteringArrayAdapter(context, R.layout.simple_dropdown_item_1line, values)
    setAdapter(adapter)
    setText(values[0], false)
    inputType = EditorInfo.TYPE_NULL
}
