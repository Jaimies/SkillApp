package com.jdevs.timeo.models

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jdevs.timeo.utilities.KeyboardUtility.Companion.hideKeyboard

open class AuthenticationFragment : Fragment() {

    fun showLoader(
        spinningProgressBar: FrameLayout,
        mainLayout: LinearLayout,
        button: Button? = null
    ) {

        hideKeyboard(activity)

        spinningProgressBar.apply {

            visibility = View.VISIBLE
        }

        mainLayout.apply {

            alpha = 0.5f
        }

        button?.apply {

            isEnabled = false
        }
    }

    fun hideLoader(
        spinningProgressBar: FrameLayout,
        mainLayout: LinearLayout,
        button: Button? = null
    ) {

        spinningProgressBar.apply {

            visibility = View.GONE
        }

        mainLayout.apply {

            alpha = 1.0f
        }

        button?.apply {

            isEnabled = true
        }
    }

    fun makeTextViewClickable(textView: TextView) {

        val signupText = textView.text

        val notClickedString = SpannableString(signupText)

        notClickedString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context!!, android.R.color.holo_blue_dark)),
            0,
            notClickedString.length,
            0
        )

        textView.setText(notClickedString, TextView.BufferType.SPANNABLE)

        val clickedString = SpannableString(notClickedString)
        clickedString.setSpan(
            ForegroundColorSpan(Color.BLUE), 0, notClickedString.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        textView.apply {

            setOnTouchListener { v, event ->

                when (event.action) {

                    MotionEvent.ACTION_DOWN -> textView.text = clickedString

                    MotionEvent.ACTION_UP -> {

                        textView.setText(notClickedString, TextView.BufferType.SPANNABLE)
                        v.performClick()
                    }

                    MotionEvent.ACTION_CANCEL -> textView.setText(

                        notClickedString,
                        TextView.BufferType.SPANNABLE

                    )
                }

                true
            }
        }
    }

    companion object {

        const val RC_SIGN_IN = 101

        fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
