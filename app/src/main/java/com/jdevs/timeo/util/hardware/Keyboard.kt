package com.jdevs.timeo.util.hardware

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.hideKeyboard() = view?.hideKeyboard()
