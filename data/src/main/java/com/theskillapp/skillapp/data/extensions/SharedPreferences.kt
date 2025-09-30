package com.theskillapp.skillapp.data.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Context.sharedPrefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun SharedPreferences.getStringPreference(name: String, defValue: String): String {
    return getString(name, defValue) ?: defValue
}

fun SharedPreferences.getStringFlow(key: String): Flow<String?> {
    return callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            trySend(getString(key, null))
        }
        registerOnSharedPreferenceChangeListener(listener)
        trySend(getString(key, null))
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }
}
