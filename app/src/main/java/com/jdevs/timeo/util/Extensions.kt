package com.jdevs.timeo.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun <T> lazyUnsynchronized(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun TextWatcher.removeSelfFrom(editText: EditText) {
    editText.removeTextChangedListener(this)
}

fun EditText.doOnceAfterTextChanged(block: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            block()
            removeSelfFrom(this@doOnceAfterTextChanged)
        }
    })
}