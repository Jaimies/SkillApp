package com.theskillapp.skillapp.shared.extensions

import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.AttrRes
import androidx.core.graphics.ColorUtils
import com.google.android.material.R

val Context.textColor get() = getColorAttributeValue(android.R.attr.textColorPrimary)
val Context.primaryColor get() = getColorAttributeValue(R.attr.colorPrimary)

fun Context.getColorAttributeValue(@AttrRes id: Int): Int {
    return getAttributeValue(id) { it.getColor(0, 0) }
}

fun Context.getDimensionAttribute(@AttrRes id: Int): Int {
    return getAttributeValue(id) { it.getDimensionPixelSize(0, 0) }
}

fun Context.getColorAttributeValueWithAlpha(@AttrRes id: Int, alpha: Int): Int {
    return getColorAttributeValue(id).let { color ->
        ColorUtils.setAlphaComponent(color, alpha)
    }
}

fun <T> Context.getAttributeValue(@AttrRes id: Int, getValue: (TypedArray) -> T): T {
    val result = obtainStyledAttributes(intArrayOf(id))
    val value = getValue(result)
    result.recycle()
    return value
}
