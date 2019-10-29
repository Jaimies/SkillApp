package com.jdevs.timeo.model

import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.jdevs.timeo.helpers.KeyboardHelper

open class LoaderFragment : FragmentWithActionBarNavigation() {

    open fun showLoader(spinningProgressBar : FrameLayout, mainLayout : LinearLayout, button : Button? = null) {

        hideKeyboard(activity)

        spinningProgressBar.apply{

            visibility = View.VISIBLE

            alpha = 1.0f

        }

        mainLayout.apply {

            alpha  = 0.5f

        }

        button?.apply {

            isEnabled = false

        }

    }

    open fun hideLoader(spinningProgressBar : FrameLayout, mainLayout : LinearLayout, button : Button? = null) {

        spinningProgressBar.apply {

            visibility = View.INVISIBLE

            alpha = 0.0f

        }

        mainLayout.apply {

            alpha  = 1.0f

        }

        button?.apply {

            isEnabled = true

        }

    }

    companion object : KeyboardHelper()

}