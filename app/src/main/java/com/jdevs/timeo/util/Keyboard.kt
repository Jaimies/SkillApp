package com.jdevs.timeo.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity?) {
    if (activity == null) {
        return
    }

    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    // Find the currently focused view, so we can grab the correct window token from it.
    val view = activity.currentFocus ?: View(activity)

    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
