package com.maxpoliakov.skillapp.util.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

val Context.sharedPrefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun SharedPreferences.getIntPreference(name: String, defValue: Int): Int {
    return getInt(name, defValue)
}

fun SharedPreferences.saveIntPreference(name: String, value: Int) {
    this.edit { putInt(name, value) }
}

fun SharedPreferences.getStringPreference(name: String, defValue: String): String {
    return getString(name, defValue) ?: defValue
}
