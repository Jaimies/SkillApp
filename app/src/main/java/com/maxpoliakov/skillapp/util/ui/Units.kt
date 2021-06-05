package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.util.DisplayMetrics

fun Float.sp(context: Context): Float {
    val px = this * context.resources.displayMetrics.scaledDensity
    return (px / context.resources.displayMetrics.density)
}

fun Float.dp(context: Context) : Float {
    return this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
