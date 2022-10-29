package com.maxpoliakov.skillapp.ui.intro

import android.content.SharedPreferences
import androidx.core.content.edit
import com.maxpoliakov.skillapp.model.Intro
import javax.inject.Inject

class IntroUtilImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : IntroUtil {
    private val introToBeShown
        get() = Intro.values()
            .filterNot(this::hasBeenShown)
            .firstOrNull()

    override fun showIfNecessary(intro: Intro, action: () -> Unit) {
        if (intro != introToBeShown) return

        action()
        markAllAsShown()
    }

    private fun hasBeenShown(intro: Intro): Boolean {
        return sharedPreferences.getBoolean(intro.preferenceName, false)
    }

    private fun markAsShown(intro: Intro) {
        sharedPreferences.edit { putBoolean(intro.preferenceName, true) }
    }

    private fun markAllAsShown() = Intro.values().forEach(this::markAsShown)
}
