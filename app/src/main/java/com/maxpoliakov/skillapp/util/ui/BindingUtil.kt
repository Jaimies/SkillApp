@file:JvmName("BindingUtil")

package com.maxpoliakov.skillapp.util.ui

import android.content.Context

fun Context.getOrReturnString(value: Any): String {
    if (value is String) return value
    if (value is Int) return getString(value)
    throw IllegalArgumentException("expected value to String or Int, received $value")
}
