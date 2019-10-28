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
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.fragment_login.view.*


const val TAG = "Login"

const val ERROR_EMPTY = 1220
const val ERROR_INVALID = 1221

const val ERROR_TOO_SHORT = 1223
const val ERROR_TOO_LONG = 1224

const val RESULT_VALID = 1222


class LoginFragment : Fragment(),
    View.OnClickListener,
    OnCompleteListener<AuthResult> {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordTextInputLayout : TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var loginButton: Button
    private lateinit var signupTextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null) {

            gotoMainActivity()

        }
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailTextInputLayout = view.emailTextInputLayout
        emailEditText = view.emailEditText

        passwordTextInputLayout = view.passwordTextInputLayout
        passwordEditText = view.passwordEditText

        loginButton = view.loginButton.apply {

            setOnClickListener(this@LoginFragment)

        }


        signupTextView = view.signupTextView.apply {

            setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)

            }
        }
        makeTextViewClickable()


        // Inflate the layout for this fragment
        return view
    }



    override fun onClick(v: View?) {

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()


        if(email.isEmpty()) {

            setError(emailTextInputLayout, emailEditText, "Email must not be empty")

            return

        }

        if(!isEmailValid(email)) {

            setError(emailTextInputLayout, emailEditText, "Email is invalid")

            return

        }

        if(password.isEmpty()) {

            setError(passwordTextInputLayout, passwordEditText,"Password must not be empty")

            return

        }


        signIn(email, password)

    }



    override fun onComplete(task: Task<AuthResult>) {

        if(task.isSuccessful) {

            Log.i(TAG, "Successfully logged in")

            gotoMainActivity()

        } else if(task.exception != null) {

            if(task.exception is FirebaseAuthInvalidCredentialsException) {

                passwordEditText.setText("")

                setError(passwordTextInputLayout, passwordEditText,"Incorrect credentials")

            } else {

                Log.i(TAG, "FAiled to sign in", task.exception)

            }

        }

    }




    private fun makeTextViewClickable() {

        val signupText = signupTextView.text

        val notClickedString = SpannableString(signupText)

        notClickedString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context!!, android.R.color.holo_blue_dark)),
            0,
            notClickedString.length,
            0
        )

        signupTextView.setText(notClickedString, TextView.BufferType.SPANNABLE)

        val clickedString = SpannableString(notClickedString)
        clickedString.setSpan(
            ForegroundColorSpan(Color.BLUE), 0, notClickedString.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )



        signupTextView.apply {

            setOnTouchListener { v, event ->

                when (event.action) {

                    MotionEvent.ACTION_DOWN -> signupTextView.text = clickedString

                    MotionEvent.ACTION_UP -> {

                        signupTextView.setText(notClickedString, TextView.BufferType.SPANNABLE)
                        v.performClick()

                    }

                    MotionEvent.ACTION_CANCEL -> signupTextView.setText(

                        notClickedString,
                        TextView.BufferType.SPANNABLE

                    )

                }

                true
            }

        }

    }




    private fun gotoMainActivity() {

        val intent = Intent(activity, MainActivity::class.java)

        startActivity(intent)

        activity?.finish()

    }




    private fun setError(inputLayout: TextInputLayout, editText: EditText, error : String) {

        if(inputLayout != emailTextInputLayout) {

            removeErrorMessage(emailTextInputLayout)

        }

        if(inputLayout != passwordTextInputLayout) {

            removeErrorMessage(passwordTextInputLayout)

        }

        inputLayout.error = error

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(s.isNotEmpty()) {

                    removeErrorMessage(emailTextInputLayout)

                    emailEditText.removeTextChangedListener(this)

                }

            }


        })

    }

    private fun removeErrorMessage(inputLayout: TextInputLayout) {

        inputLayout.apply {

            error = ""

            isErrorEnabled = false

        }

    }


    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)

    }


    companion object {

        private fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }

    }
}
