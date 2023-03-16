package com.maxpoliakov.skillapp.shared.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.graphics.ColorUtils
import com.maxpoliakov.skillapp.R

val Context.textColor get() = getColorAttributeValue(android.R.attr.textColorPrimary)
val Context.primaryColor get() = getColorAttributeValue(R.attr.colorPrimary)

fun Context.getColorAttributeValue(@AttrRes id: Int): Int {
    val typedValue = TypedValue()

    val result = obtainStyledAttributes(typedValue.data, intArrayOf(id))
    val color = result.getColor(0, 0)

    result.recycle()

    return color
}

fun Context.getColorAttributeValueWithAlpha(@AttrRes id: Int, alpha: Int): Int {
    return getColorAttributeValue(id).let { color ->
        ColorUtils.setAlphaComponent(color, alpha)
    }
}
