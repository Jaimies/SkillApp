package com.jdevs.timeo.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
