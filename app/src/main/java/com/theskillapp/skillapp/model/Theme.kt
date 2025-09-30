package com.theskillapp.skillapp.model

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

enum class Theme(val value: Int) {
    Light(MODE_NIGHT_NO),
    Dark(MODE_NIGHT_YES),
    Auto(MODE_NIGHT_FOLLOW_SYSTEM)
}
