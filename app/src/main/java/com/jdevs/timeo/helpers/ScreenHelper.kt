package com.jdevs.timeo.helpers

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager

open class ScreenHelper {
    companion object {
        fun getDimensions(context: Context) : DisplayMetrics {

            val displayMetrics = DisplayMetrics()


            val windowService = context.getSystemService(WINDOW_SERVICE) as WindowManager


            windowService.defaultDisplay.apply {

                getMetrics(displayMetrics)

            }


            return displayMetrics

        }
    }
}