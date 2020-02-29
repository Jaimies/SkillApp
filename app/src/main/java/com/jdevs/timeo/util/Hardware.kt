package com.jdevs.timeo.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

fun Context.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, 0)
}

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.hideKeyboard() = view?.hideKeyboard()
