package com.maxpoliakov.skillapp

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class PremiumIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))

        addSlide(
            AppIntroFragment.newInstance(
                title = "Drag skills to group them",
                backgroundColor = Color.parseColor("#0000c9"),
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                description = "Drag a skill over another one to group them",
                imageDrawable = R.drawable.ic_check,
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Backup your data",
                backgroundColor = Color.parseColor("#fc6f00"),
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                description = "Go to Settings > Backup & Restore",
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
