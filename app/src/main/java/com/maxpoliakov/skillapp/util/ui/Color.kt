package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.util.TypedValue

fun Context.getTextColor(): Int {
    val typedValue = TypedValue()

    val result = obtainStyledAttributes(typedValue.data, intArrayOf(android.R.attr.textColorPrimary))
    val color = result.getColor(0, 0)

    result.recycle()

    return color
}
