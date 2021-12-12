package com.maxpoliakov.skillapp.ui.premium

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.maxpoliakov.skillapp.R

class PremiumIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        addSlide(R.string.premium_intro_slide1_title, R.string.premium_intro_slide1_description)
        addSlide(R.string.premium_intro_slide2_title, R.string.premium_intro_slide2_description, "premium_intro_group_skills")
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
}

private fun addAlpha(@ColorInt textColor: Int, alpha: Int) =
    Color.argb(alpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor))
