package com.jdevs.timeo.util.hardware

import android.content.Context

fun Context.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()
