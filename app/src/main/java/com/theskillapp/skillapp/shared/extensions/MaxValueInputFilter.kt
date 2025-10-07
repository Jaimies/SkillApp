package com.theskillapp.skillapp.shared.extensions

import android.text.InputFilter
import android.text.Spanned

// prevents values greater than the max value from being typed into the EditText
class MaxValueInputFilter(private val getMaxValue: () -> Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val replacement = source.substring(start, end)
        if (replacement.isEmpty()) return null

        val newString = dest.substring(0, dstart) + replacement + dest.substring(dend)
        val newVal = newString.toIntOrNull() ?: return null

        if (newVal > getMaxValue()) {
            // reject the input
            return ""
        }

        return null
    }
}
