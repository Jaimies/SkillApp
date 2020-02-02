@file:Suppress("NOTHING_TO_INLINE")

package com.jdevs.timeo.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Context.getScreenDimensions(): DisplayMetrics {

    val displayMetrics = DisplayMetrics()
    val windowService = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    windowService.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

fun Context.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

inline fun Fragment.hideKeyboard() = view?.hideKeyboard()
