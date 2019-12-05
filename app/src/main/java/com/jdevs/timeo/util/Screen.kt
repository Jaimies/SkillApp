package com.jdevs.timeo.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager

fun getScreenDimensions(context: Context): DisplayMetrics {

    val displayMetrics = DisplayMetrics()

    val windowService = context.getSystemService(WINDOW_SERVICE) as WindowManager

    windowService.defaultDisplay.apply {

        getMetrics(displayMetrics)
    }

    return displayMetrics
}
