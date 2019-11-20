package com.jdevs.timeo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.models.AuthFragment
import com.jdevs.timeo.util.Keyboard.Companion.hide
import com.jdevs.timeo.util.TAG
import kotlinx.android.synthetic.main.fragment_signup.view.emailEditText
import kotlinx.android.synthetic.main.fragment_signup.view.emailTextInputLayout
import kotlinx.android.synthetic.main.fragment_signup.view.loginButton
import kotlinx.android.synthetic.main.fragment_signup.view.loginTextView
import kotlinx.android.synthetic.main.fragment_signup.view.mainLayout
import kotlinx.android.synthetic.main.fragment_signup.view.passwordEditText
import kotlinx.android.synthetic.main.fragment_signup.view.passwordTextInputLayout
import kotlinx.android.synthetic.main.circular_loader.view.spinningProgressBar

class SignupFragment : AuthFragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var mEmailEditText: EditText
    private lateinit var mEmailTextInputLayout: TextInputLayout
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordTextInputLayout: TextInputLayout

    private lateinit var mLoader: FrameLayout
    private lateinit var mSignupButton: Button
    private lateinit var mMainLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        view.apply {

            mEmailEditText = emailEditText

            mEmailTextInputLayout = emailTextInputLayout

            mPasswordEditText = passwordEditText.apply {

                setOnKeyListener(this@SignupFragment)
            }

            mPasswordTextInputLayout = passwordTextInputLayout

            mSignupButton = loginButton.apply {

                setOnClickListener(this@SignupFragment)
            }

            mLoader = spinningProgressBar

            loginTextView.setOnClickListener {

                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }

            mMainLayout = mainLayout

            makeTextViewClickable(loginTextView)

            rootView.setOnClickListener {

                hide(activity)
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onClick(view: View?) {

        if (!validateInput(
                mEmailTextInputLayout,
                mEmailEditText,
                ::validateEmailString,
                ::showEmailError
            )
        ) {

            return
        }

        if (!validateInput(
                mPasswordTextInputLayout,
                mPasswordEditText,
                ::validatePasswordString,
                ::showPasswordError
            )
        ) {

            return
        }

        val email = mEmailEditText.text.toString()
        val password = mPasswordEditText.text.toString()

        signUp(email, password)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.action == KeyEvent.ACTION_DOWN) {

            onClick(v)
        }

        return false
    }

    override fun onComplete(task: Task<AuthResult>) {

        hideLoader()

        if (task.isSuccessful) {

            goToMainActivity()

            return
        }

        if (task.exception == null) {

            return
        }

        when (task.exception) {

            is FirebaseAuthWeakPasswordException -> {

                setError(
                    mPasswordTextInputLayout,
                    mPasswordEditText,
                    "The password is too weak"
                )
            }

            is FirebaseAuthUserCollisionException -> {

                setError(
                    mEmailTextInputLayout,
                    mEmailEditText,
                    "User with that email already exists"
                )
            }

            is FirebaseAuthInvalidCredentialsException -> {

                setError(mEmailTextInputLayout, mEmailEditText, "Email is invalid")
            }

            else -> {

                Log.w(TAG, "Failed to sign up", task.exception)

                Toast.makeText(context, "Failed to sign in", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateInput(
        textInputLayout: TextInputLayout,
        editText: EditText,
        validator: (String) -> Int,
        errorHandler: (Int) -> Unit
    ): Boolean {

        val value = editText.text.toString()

        val validationResult = validator(value)

        if (validationResult != R.id.RESULT_VALID) {

            errorHandler(validationResult)

            editText.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (validator(s.toString()) != validationResult) {

                        textInputLayout.error = ""

                        editText.removeTextChangedListener(this)
                    }
                }
            })

            return false
        }

        return true
    }

    private fun goToMainActivity() {

        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
    }

    private fun showPasswordError(result: Int) {

        if (result == R.id.RESULT_VALID) {

            mPasswordTextInputLayout.error = ""

            return
        }

        val error = when (result) {

            R.id.ERROR_EMPTY -> "Password must not be empty"
            R.id.ERROR_TOO_SHORT -> "Password must be at least 6 characters long"
            R.id.ERROR_TOO_LONG -> "Password length must not exceed 25 characters"
            else -> "Password is not valid"
        }

        setError(mPasswordTextInputLayout, mPasswordEditText, error)
    }

    private fun showEmailError(result: Int) {

        if (result == R.id.RESULT_VALID) {

            mEmailTextInputLayout.error = ""

            return
        }

        val error = when (result) {

            R.id.ERROR_EMPTY -> "Email must not be empty"
            else -> "Email is not valid"
        }

        setError(mEmailTextInputLayout, mEmailEditText, error)
    }

    private fun setError(inputLayout: TextInputLayout, editText: EditText, error: String) {

        if (inputLayout != mEmailTextInputLayout) {

            removeErrorMessage(inputLayout)
        }

        if (inputLayout != mPasswordTextInputLayout) {

            removeErrorMessage(inputLayout)
        }

        inputLayout.error = error

        editText.apply {

            requestFocus()
            setSelection(this.length())
        }
    }

    private fun removeErrorMessage(inputLayout: TextInputLayout) {

        inputLayout.apply {

            error = ""

            isErrorEnabled = false
        }
    }

    private fun showLoader() {

        super.showLoader(mLoader, mMainLayout, mSignupButton)
    }

    private fun hideLoader() {

        super.hideLoader(mLoader, mMainLayout, mSignupButton)
    }

    private fun signUp(email: String, password: String) {

        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this)

        showLoader()
    }

    companion object {

        private fun validatePasswordString(password: String): Int {

            if (password.isEmpty()) {

                return R.id.ERROR_EMPTY
            }

            if (password.length < 6) {

                return R.id.ERROR_TOO_SHORT
            }

            if (password.length > 25) {

                return R.id.ERROR_TOO_LONG
            }

            return R.id.RESULT_VALID
        }

        private fun validateEmailString(email: String): Int {

            if (email.isEmpty()) {

                return R.id.ERROR_EMPTY
            }

            if (!isEmailValid(email)) {

                return R.id.ERROR_INVALID
            }

            return R.id.RESULT_VALID
        }
    }
}
