package com.maxpoliakov.skillapp.util.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.isVisible(value: Boolean) {
    isVisible = value
}
