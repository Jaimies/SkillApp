package com.maxpoliakov.skillapp.util.ui

import android.text.Spannable
import android.text.SpannableStringBuilder

fun SpannableStringBuilder.setSpanForWholeString(what: Any) {
    setSpan(what, 0, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
}
