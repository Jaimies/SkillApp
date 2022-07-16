package com.maxpoliakov.skillapp.ui.intro

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.maxpoliakov.skillapp.R
import java.time.LocalDate
import java.time.Month

class TheIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        if (shouldShowTheIntroSlide())
            addSlide(R.string.intro_slide1_title, R.string.intro_slide1_description)

        addSlide(R.string.intro_slide2_title, R.string.intro_slide2_description, "intro_grouping")
        addSlide(R.string.intro_slide3_title, R.string.intro_slide3_description, "intro_units")
        addSlide(R.string.intro_slide4_title, R.string.intro_slide4_description, "intro_backups")
        isWizardMode = true
    }

    private fun shouldShowTheIntroSlide(): Boolean {
        return LocalDate.now() < LocalDate.of(2022, Month.AUGUST, 15)
    }

    private fun addSlide(@StringRes titleResId: Int, @StringRes descriptionResId: Int, videoFileName: String = "") {
        addSlide(IntroSlide(titleResId, descriptionResId, videoFileName))
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
