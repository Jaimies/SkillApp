package com.jdevs.timeo


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.emailEditText
import kotlinx.android.synthetic.main.fragment_login.view.emailTextInputLayout
import kotlinx.android.synthetic.main.fragment_login.view.loginButton
import kotlinx.android.synthetic.main.fragment_login.view.passwordEditText
import kotlinx.android.synthetic.main.fragment_login.view.passwordTextInputLayout
import kotlinx.android.synthetic.main.fragment_signup.view.*

/**
 * A simple [Fragment] subclass.
 */
class SignupFragment : Fragment(),
    View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordTextInputLayout : TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var loginButton: Button
    private lateinit var loginTextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        emailTextInputLayout = view.emailTextInputLayout
        emailEditText = view.emailEditText

        passwordTextInputLayout = view.passwordTextInputLayout
        passwordEditText = view.passwordEditText

        loginButton = view.loginButton.apply {

            setOnClickListener(this@SignupFragment)

        }

        loginTextView = view.loginTextView

        val signupText = loginTextView.text

        val notClickedString = SpannableString(signupText)

        notClickedString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context!!, android.R.color.holo_blue_dark)),
            0,
            notClickedString.length,
            0
        )

        loginTextView.setText(notClickedString, TextView.BufferType.SPANNABLE)

        val clickedString = SpannableString(notClickedString)
        clickedString.setSpan(
            ForegroundColorSpan(Color.BLUE), 0, notClickedString.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        loginTextView.apply {

            setOnTouchListener { v, event ->

                when (event.action) {

                    MotionEvent.ACTION_DOWN -> this@SignupFragment.loginTextView.text = clickedString

                    MotionEvent.ACTION_UP -> {

                        this@SignupFragment.loginTextView.setText(notClickedString, TextView.BufferType.SPANNABLE)
                        v.performClick()

                    }

                    MotionEvent.ACTION_CANCEL -> signupTextView.setText(

                        notClickedString,
                        TextView.BufferType.SPANNABLE

                    )

                }

                true
            }

            setOnClickListener {

                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

            }

        }

        // Inflate the layout for this fragment
        return view
    }





    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user != null) {

            gotoMainActivity()

        }

    }

    override fun onClick(v: View?) {

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()


        val emailValidationResult = validateEmailString(email)

        if(emailValidationResult != RESULT_VALID) {

            showEmailError(emailValidationResult)

            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if(validateEmailString(s.toString()) != emailValidationResult) {

                        removeErrorMessage(emailTextInputLayout)

                        emailEditText.removeTextChangedListener(this)

                    }

                }


            })

            return

        }


        val passwordValidationResult = validatePasswordString(password)

        if(passwordValidationResult != RESULT_VALID) {
            showPasswordError(passwordValidationResult)

            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if(validatePasswordString(s.toString()) != passwordValidationResult) {

                        passwordTextInputLayout.error = ""

                        passwordEditText.removeTextChangedListener(this)

                    }

                }


            })

            return

        }


        signIn(email, password)

    }




    private fun gotoMainActivity() {

        val intent = Intent(activity, MainActivity::class.java)

        startActivity(intent)

        activity?.finish()

    }


    private fun showPasswordError(result : Int) {

        if(result == RESULT_VALID) {

            passwordTextInputLayout.error = ""

            return

        }

        val error = when(result) {

            ERROR_EMPTY -> "Password must not be empty"
            ERROR_TOO_SHORT -> "Password must be at least 6 characters long"
            ERROR_TOO_LONG -> "Password length must not exceed 25 characters"
            else -> "Password is not valid"

        }

        setError(passwordTextInputLayout, error)

    }

    private fun showEmailError(result : Int) {

        if(result == RESULT_VALID) {

            emailTextInputLayout.error = ""

            return

        }

        val error = when(result) {

            ERROR_EMPTY -> "Email must not be empty"
            else -> "Email is not valid"

        }

        setError(emailTextInputLayout, error)

    }



    private fun setError(inputLayout: TextInputLayout, error : String) {

        if(inputLayout != emailTextInputLayout) {

            removeErrorMessage(inputLayout)

        }

        if(inputLayout != passwordTextInputLayout) {

           removeErrorMessage(inputLayout)

        }

        inputLayout.error = error

    }

    private fun removeErrorMessage(inputLayout: TextInputLayout) {

        inputLayout.apply {

            error = ""

            isErrorEnabled = false

        }

    }

    private fun signIn(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if(task.isSuccessful) {

                    Log.i(TAG, "Successfully logged in")

                    gotoMainActivity()

                } else if(task.exception != null) {

                    Log.w(TAG, "Failed to sign up", task.exception)

                }

            }

    }


    companion object {

        private fun validatePasswordString(password : String) : Int {

            if(password.isEmpty()) {

                return ERROR_EMPTY

            }

            if(password.length < 6) {

                return ERROR_TOO_SHORT

            }

            if(password.length > 25) {

                return ERROR_TOO_LONG

            }

            return RESULT_VALID

        }


        private fun validateEmailString(email : String) : Int {


            if(email.isEmpty()) {

                return ERROR_EMPTY

            }

            if(!isEmailValid(email)) {

                return ERROR_INVALID

            }

            return RESULT_VALID

        }


        private fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }


    }


}
