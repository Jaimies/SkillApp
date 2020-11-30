package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import com.maxpoliakov.skillapp.R

fun Context.getTextColor() = getColorAttributeValue(android.R.attr.textColorPrimary)
fun Context.getPrimaryColor() = getColorAttributeValue(R.attr.colorPrimary)

fun Context.getColorAttributeValue(@AttrRes id: Int): Int {
    val typedValue = TypedValue()

    val result = obtainStyledAttributes(typedValue.data, intArrayOf(id))
    val color = result.getColor(0, 0)

    result.recycle()

    return color
}
