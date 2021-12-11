package com.maxpoliakov.skillapp

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class PremiumIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        addSlide("Thank you for choosing SkillApp Premium", "Hope you enjoy it")
        addSlide("Drag skills to group them", "Drag one skill over another to group them")
        addSlide("Backup your data", "Go to Settings > Backup & Restore")
        addSlide("Manage your subscription", "Go to Settings > SkillApp Premium")
    }

    private fun addSlide(title: String, description: String) {
        addSlide(
            AppIntroFragment.newInstance(
                title = title,
                backgroundColor = ContextCompat.getColor(this, R.color.black),
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                description = description,
                imageDrawable = R.drawable.ic_lock,
            )
        )
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
