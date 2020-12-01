package com.maxpoliakov.skillapp.util.ui

import android.content.Context

fun Int.sp(context: Context): Float {
    val px = this * context.resources.displayMetrics.scaledDensity
    return (px / context.resources.displayMetrics.density)
}
