package com.theskillapp.skillapp.shared.hardware

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.core.content.getSystemService

fun View.showKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() = window.decorView.hideKeyboard()
