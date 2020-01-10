package com.jdevs.timeo.util

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.jdevs.timeo.util.extensions.doOnceAfterTextChanged

@BindingAdapter("hideIf")
fun hideIf(view: View, shouldHide: Boolean) {

    view.visibility = if (shouldHide) View.GONE else View.VISIBLE
}

@BindingAdapter("error", "editText", requireAll = true)
fun setError(textInputLayout: TextInputLayout, error: String, editText: EditText) {

    if (error.isEmpty()) {

        textInputLayout.isErrorEnabled = false
        return
    }

    textInputLayout.error = error

    editText.apply {

        requestFocus()
        setSelection(length())

        doOnceAfterTextChanged {

            textInputLayout.isErrorEnabled = false
        }
    }
}

@BindingAdapter("onEnterPressed")
fun setOnEnterPressedListener(view: View, block: Runnable) {

    view.setOnKeyListener { _, keyCode, event ->

        if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {

            block.run()
            return@setOnKeyListener true
        }

        false
    }
}

@BindingAdapter("android:onClick")
fun setOnClickListener(button: SignInButton, block: Runnable) {

    button.setOnClickListener { block.run() }
}
