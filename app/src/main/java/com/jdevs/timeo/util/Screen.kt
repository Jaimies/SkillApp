package com.jdevs.timeo.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager

fun Context.getScreenDimensions(): DisplayMetrics {

    val displayMetrics = DisplayMetrics()

    val windowService = getSystemService(WINDOW_SERVICE) as WindowManager

    windowService.defaultDisplay.apply {

        getMetrics(displayMetrics)
    }

    return displayMetrics
}
