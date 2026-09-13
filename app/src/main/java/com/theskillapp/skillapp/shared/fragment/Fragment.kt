package com.theskillapp.skillapp.shared.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline onChange: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { onChange(it) }
}

fun Fragment.addKeepScreenOnFlag() {
    requireActivity().window.addFlags(FLAG_KEEP_SCREEN_ON)
}

fun Fragment.removeKeepScreenOnFlag() {
    requireActivity().window.clearFlags(FLAG_KEEP_SCREEN_ON)
}

