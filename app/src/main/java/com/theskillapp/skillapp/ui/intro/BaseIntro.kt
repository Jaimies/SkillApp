package com.theskillapp.skillapp.ui.intro

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.github.appintro.AppIntro
import com.theskillapp.skillapp.R

abstract class BaseIntro : AppIntro() {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setColorDoneText(Color.WHITE)
        setColorSkipButton(Color.WHITE)
        setIndicatorColor(addAlpha(Color.WHITE, 180), addAlpha(Color.WHITE, 100))
        setDoneText(R.string.got_it)
        isWizardMode = true
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }

    protected fun addSlide(@StringRes titleResId: Int, @StringRes descriptionResId: Int, videoFileName: String = "") {
        addSlide(
            IntroSlide.newInstance(titleResId, descriptionResId, videoFileName)
        )
    }
}

private fun addAlpha(@ColorInt textColor: Int, alpha: Int) =
    Color.argb(alpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor))
