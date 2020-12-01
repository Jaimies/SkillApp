package com.maxpoliakov.skillapp.util.persistence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

private val Context.prefs: SharedPreferences
    get() = getSharedPreferences("main", MODE_PRIVATE)

fun Context.getIntPreference(name: String, defValue: Int): Int {
    return prefs.getInt(name, defValue)
}

fun Context.saveIntPreference(name: String, value: Int) {
    prefs.edit { putInt(name, value) }
}
