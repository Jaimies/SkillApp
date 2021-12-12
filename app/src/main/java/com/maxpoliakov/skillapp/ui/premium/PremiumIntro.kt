package com.maxpoliakov.skillapp.ui.premium

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro

class PremiumIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        addSlide("Thank you for choosing SkillApp Premium", "Manager your subscription under Settings\u00A0>\u00A0SkillApp Premium")
        addSlide("Drag skills to group them", "Drag one skill on top of another to group them", "premium_intro_group_skills")
        addSlide("Backup your data", "Go to Settings\u00A0>\u00A0Backup & Restore and sign in to your Google Account", "premium_intro_backups")
        isWizardMode = true
    }

    private fun addSlide(title: String, description: String, videoFileName: String = "") {
        addSlide(PremiumIntroSlide(title, description, videoFileName))
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
