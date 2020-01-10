package com.jdevs.timeo.util

import android.app.Activity
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

fun Activity.hideKeyboard() {

    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)

    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() = activity?.hideKeyboard()
