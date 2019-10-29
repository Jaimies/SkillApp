package com.jdevs.timeo.helpers

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

open class KeyboardHelper {

    fun hideKeyboard(activity : Activity?) {

        if(activity == null) {

            return

        }

        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {

            view = View(activity)

        }

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }

}