package com.jdevs.timeo

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager

class ScreenManager {
    companion object {
        fun getScreenDimensions(context: Context?) : Pair<Int, Int> {
            val displayMetrics = DisplayMetrics()
            (context!!.getSystemService(WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .apply { getMetrics(displayMetrics) }

            return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }
}