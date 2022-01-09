package com.maxpoliakov.skillapp.util.dialog

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_LONG) {
    val snackbar = Snackbar.make(this, resId, duration)
    val snackTextView = snackbar.view.findViewById<TextView>(R.id.snackbar_text)

    snackTextView.maxLines = 4
    snackbar.show()
}

fun Fragment.showSnackbar(@StringRes resId: Int) {
    requireView().showSnackbar(resId)
}
