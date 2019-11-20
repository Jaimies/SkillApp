package com.jdevs.timeo.viewmodels

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("hideIf")
fun hideIf(view: View, value: Boolean) {
    view.visibility = if (value) View.GONE else View.VISIBLE
}
