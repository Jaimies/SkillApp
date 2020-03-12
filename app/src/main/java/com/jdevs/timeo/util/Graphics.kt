package com.jdevs.timeo.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Drawable.setAutoBounds() = setBounds(0, 0, intrinsicWidth, intrinsicHeight)

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
fun Context.getDrawableCompat(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)
