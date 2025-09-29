package com.maxpoliakov.skillapp.ui.intro

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class IntroUtilImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : IntroUtil {
    override fun hasFirstRunIntroBeenShown() : Boolean {
        return sharedPreferences.getBoolean("intro_viewed", false)
    }

    override fun markFirstRunIntroAsShown() {
        sharedPreferences.edit { putBoolean("intro_viewed", true) }
    }
}
