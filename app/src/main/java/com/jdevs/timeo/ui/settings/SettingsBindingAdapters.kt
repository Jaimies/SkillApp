package com.jdevs.timeo.ui.settings

import android.widget.CompoundButton
import androidx.databinding.BindingAdapter

@BindingAdapter("checked")
fun setChecked(view: CompoundButton, isChecked: Boolean) = view.run {

    this.isChecked = isChecked

    if (tag != SETUP) {

        jumpDrawablesToCurrentState()
        tag = SETUP
    }
}

private const val SETUP = "SETUP"
