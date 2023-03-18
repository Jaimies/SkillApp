package com.maxpoliakov.skillapp.shared.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.maxpoliakov.skillapp.data.persistence.getStringPreference
import com.maxpoliakov.skillapp.data.persistence.sharedPrefs
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.shared.enum.enumHasValue

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
