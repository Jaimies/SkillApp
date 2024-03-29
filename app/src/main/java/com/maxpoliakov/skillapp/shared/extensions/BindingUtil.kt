@file:JvmName("BindingUtil")

package com.maxpoliakov.skillapp.shared.extensions

import android.content.Context

fun Context.getOrReturnString(value: Any?): String? {
    if (value == null) return null
    if (value is String) return value
    if (value is Int) return getString(value)
    throw IllegalArgumentException("expected value to String or Int, received $value")
}
