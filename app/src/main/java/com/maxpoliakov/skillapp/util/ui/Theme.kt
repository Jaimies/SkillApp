package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.maxpoliakov.skillapp.model.Theme
import com.maxpoliakov.skillapp.util.persistence.getStringPreference
import com.maxpoliakov.skillapp.util.persistence.sharedPrefs

fun setTheme(theme: Theme) {
    AppCompatDelegate.setDefaultNightMode(theme.value)
}

fun Context.setupTheme() {
    val themeString = sharedPrefs.getStringPreference("theme", "Auto")
    setTheme(Theme.valueOf(themeString))
}
