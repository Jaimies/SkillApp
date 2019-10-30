package com.jdevs.timeo.models

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jdevs.timeo.R
import com.jdevs.timeo.helpers.KeyboardHelper.Companion.hideKeyboard


open class AuthenticationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.apply {

            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            bottomNavView.visibility = View.GONE

        }


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showLoader(spinningProgressBar : FrameLayout, mainLayout : LinearLayout, button : Button? = null) {

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

    fun hideLoader(spinningProgressBar : FrameLayout, mainLayout : LinearLayout, button : Button? = null) {

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

    fun returnToMainActivity() {

        findNavController().apply {

            popBackStack(R.id.loginFragment, true)

            navigate(R.id.homeFragment)

        }

    }

}