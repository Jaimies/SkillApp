package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

abstract class Unit(val count: Float) {
    abstract fun toPx(context: Context): Int
    abstract fun toDp(context: Context): Float
}

class Dp(count: Float) : Unit(count) {
    override fun toPx(context: Context): Int {
        val pxFloat = count * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return pxFloat.roundToInt()
    }

    override fun toDp(context: Context): Float {
        return count
    }
}

class Sp(count: Float) : Unit(count) {
    override fun toDp(context: Context): Float {
        val px = count * context.resources.displayMetrics.scaledDensity
        return (px / context.resources.displayMetrics.density)
    }

    override fun toPx(context: Context): Int {
        val dp = Dp(this.toDp(context))
        return dp.toPx(context)
    }
}

val Float.dp get() = Dp(this)
val Float.sp get() = Sp(this)
val Int.dp get() = Dp(this.toFloat())
val Int.sp get() = Sp(this.toFloat())
