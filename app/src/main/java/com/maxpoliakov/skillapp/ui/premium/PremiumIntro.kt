package com.maxpoliakov.skillapp.ui.premium

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.appintro.AppIntro
import com.maxpoliakov.skillapp.R

class PremiumIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        addSlide(R.string.premium_intro_slide1_title, R.string.premium_intro_slide1_description)
        addSlide(R.string.premium_intro_slide2_title, R.string.premium_intro_slide2_description, "premium_intro_grouping")
        addSlide(R.string.premium_intro_slide3_title, R.string.premium_intro_slide3_description, "premium_intro_backups")
        isWizardMode = true
    }

    private fun addSlide(@StringRes titleResId: Int, @StringRes descriptionResId: Int, videoFileName: String = "") {
        addSlide(PremiumIntroSlide(titleResId, descriptionResId, videoFileName))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }

    companion object {
        fun showIfNeeded(activity: Activity) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

            val hasSeenTutorial = prefs.getBoolean(PREMIUM_INTRO_ALREADY_SEEN, false)

            if (hasSeenTutorial) return

            activity.startActivity(Intent(activity, PremiumIntro::class.java))
            prefs.edit { putBoolean(PREMIUM_INTRO_ALREADY_SEEN, true) }
        }

        private const val PREMIUM_INTRO_ALREADY_SEEN = "com.maxpoliakov.skillapp.PREMIUM_INTRO_ALREADY_SEEN"
    }
}

private fun addAlpha(@ColorInt textColor: Int, alpha: Int) =
    Color.argb(alpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor))
