package com.maxpoliakov.skillapp.util.dialog

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.showToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(@StringRes resId: Int) {
    requireContext().showToast(resId)
}
