package com.theskillapp.skillapp.shared.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline onChange: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { onChange(it) }
}
