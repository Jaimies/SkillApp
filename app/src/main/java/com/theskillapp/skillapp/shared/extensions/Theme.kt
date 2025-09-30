package com.theskillapp.skillapp.shared.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.theskillapp.skillapp.data.extensions.getStringPreference
import com.theskillapp.skillapp.data.extensions.sharedPrefs
import com.theskillapp.skillapp.model.Theme
import com.theskillapp.skillapp.shared.enum.enumHasValue

fun setTheme(theme: Theme) {
    AppCompatDelegate.setDefaultNightMode(theme.value)
}

fun Context.setupTheme() {
    val themeString = sharedPrefs.getStringPreference("theme", "Auto")

    if(!enumHasValue<Theme>(themeString)) {
        setTheme(Theme.Auto)
        sharedPrefs.setTheme(Theme.Auto)
    } else {
        setTheme(Theme.valueOf(themeString))
    }
}

private fun SharedPreferences.setTheme(theme: Theme) {
    edit { putString("theme", theme.toString()) }
}
