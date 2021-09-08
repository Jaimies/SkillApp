package com.maxpoliakov.skillapp.util.dialog

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(@StringRes resId: Int) {
    Snackbar.make(this, resId, Snackbar.LENGTH_LONG).show()
}

fun Fragment.showSnackbar(@StringRes resId: Int) {
    requireView().showSnackbar(resId)
}
