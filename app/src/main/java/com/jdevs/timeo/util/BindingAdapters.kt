package com.jdevs.timeo.util

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("hideIf")
fun hideIf(view: View, value: Boolean) {

    view.visibility = if (value) View.GONE else View.VISIBLE
}

@BindingAdapter("clickableStyle")
fun applyClickableStyle(view: TextView, value: Boolean) {

    if (!value) {
        return
    }

    view.apply {
        val signupText = text

        val notClickedString = SpannableString(signupText)

        notClickedString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(context, android.R.color.holo_blue_dark)
            ), 0, notClickedString.length, 0
        )

        setText(notClickedString, TextView.BufferType.SPANNABLE)

        val clickedString = SpannableString(notClickedString)
        clickedString.setSpan(
            ForegroundColorSpan(Color.BLUE), 0, notClickedString.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> text = clickedString

                MotionEvent.ACTION_UP -> {
                    setText(notClickedString, TextView.BufferType.SPANNABLE)
                    v.performClick()
                }

                MotionEvent.ACTION_CANCEL -> setText(
                    notClickedString,
                    TextView.BufferType.SPANNABLE
                )
            }

            true
        }
    }
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
        if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) &&
            event?.action == KeyEvent.ACTION_DOWN
        ) {
            block.run()
        }

        false
    }
}

@BindingAdapter("android:onClick")
fun bindSignInClick(button: SignInButton, block: Runnable) {

    button.setOnClickListener { block.run() }
}
