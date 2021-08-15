package com.maxpoliakov.skillapp.util.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.isVisible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("layout_sideMargin")
fun setSideMargins(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dimen.toInt()
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

