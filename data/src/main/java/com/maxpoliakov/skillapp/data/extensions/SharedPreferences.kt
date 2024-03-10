package com.maxpoliakov.skillapp.data.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

val Context.sharedPrefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun SharedPreferences.getStringPreference(name: String, defValue: String): String {
    return getString(name, defValue) ?: defValue
}
