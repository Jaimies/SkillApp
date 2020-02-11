package com.jdevs.timeo.util

import android.graphics.drawable.Drawable

fun Drawable.setAutoBounds() = setBounds(0, 0, intrinsicWidth, intrinsicHeight)
