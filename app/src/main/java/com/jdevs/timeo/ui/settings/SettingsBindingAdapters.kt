package com.jdevs.timeo.ui.settings

import android.widget.CompoundButton
import androidx.databinding.BindingAdapter

@BindingAdapter("checked")
fun setChecked(view: CompoundButton, isChecked: Boolean) {

    view.isChecked = isChecked
    view.jumpDrawablesToCurrentState()
}
