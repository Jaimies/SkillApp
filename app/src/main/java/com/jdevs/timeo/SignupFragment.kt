package com.jdevs.timeo


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import com.jdevs.timeo.helpers.KeyboardHelper.Companion.hideKeyboard
import com.jdevs.timeo.models.AuthenticationFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*


class SignupFragment : AuthenticationFragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private val auth = FirebaseAuth.getInstance()

    private val args by navArgs<SignupFragmentArgs>()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LoginFragment.RC_SIGN_IN) {

            hideLoader()

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java) ?: return

                firebaseLinkGoogleAccount(account)

            } catch (e: ApiException) {

                if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {

                    Log.i(TAG, "Sign in was cancelled by user")

                    return

                }

                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Snackbar.make(view!!, "Failed to link account with Google", Snackbar.LENGTH_LONG).show()

            }

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)


        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        view.apply {

            passwordEditText.setOnKeyListener(this@SignupFragment)

            signupButton.setOnClickListener(this@SignupFragment)

            loginTextView.apply loginTextView@{

                if (args.linkAccount) {

                    visibility = View.GONE

                    return@loginTextView
                }

                setOnClickListener {

                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    requireActivity().toolbar.navigationIcon = null

                }

                makeTextViewClickable(this)
            }


            if (args.linkAccount) {

                googleSignInButton.apply {

                    visibility = View.VISIBLE

                    setOnClickListener {

                        showLoader()

                        showGoogleSignInIntent()

                    }

                }

            }

            rootView.setOnClickListener {

                hideKeyboard(activity)

            }

        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onClick(view: View?) {

        if (!validateInput(
                getView()!!.emailTextInputLayout,
                getView()!!.emailEditText,
                ::validateEmailString,
                ::showEmailError
            )
        ) {

            return

        }

        if (!validateInput(
                getView()!!.passwordTextInputLayout,
                getView()!!.passwordEditText,
                ::validatePasswordString,
                ::showPasswordError
            )
        ) {

            return

        }

        val email = getView()!!.emailEditText.text.toString()
        val password = getView()!!.passwordEditText.text.toString()

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
                    view!!.passwordTextInputLayout,
                    view!!.passwordEditText,
                    "The password is too weak"
                )

            }

            is FirebaseAuthUserCollisionException -> {

                setError(
                    view!!.emailTextInputLayout,
                    view!!.emailEditText,
                    "User with that email already exists"
                )

            }

            is FirebaseAuthInvalidCredentialsException -> {

                setError(view!!.emailTextInputLayout, view!!.emailEditText, "Email is invalid")

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

        if (validationResult != RESULT_VALID) {

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

        if (result == RESULT_VALID) {

            view!!.passwordTextInputLayout.error = ""

            return

        }

        val error = when (result) {

            ERROR_EMPTY -> "Password must not be empty"
            ERROR_TOO_SHORT -> "Password must be at least 6 characters long"
            ERROR_TOO_LONG -> "Password length must not exceed 25 characters"
            else -> "Password is not valid"

        }

        setError(view!!.passwordTextInputLayout, view!!.passwordEditText, error)

    }


    private fun showEmailError(result: Int) {

        if (result == RESULT_VALID) {

            view!!.emailTextInputLayout.error = ""

            return

        }

        val error = when (result) {

            ERROR_EMPTY -> "Email must not be empty"
            else -> "Email is not valid"

        }

        setError(view!!.emailTextInputLayout, view!!.emailEditText, error)

    }


    private fun setError(inputLayout: TextInputLayout, editText: EditText, error: String) {

        if (inputLayout != view!!.emailTextInputLayout) {

            removeErrorMessage(inputLayout)

        }

        if (inputLayout != view!!.passwordTextInputLayout) {

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

    private fun firebaseLinkGoogleAccount(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        showLoader()

        auth.currentUser?.apply {
            linkWithCredential(credential)
                .addOnCompleteListener {

                    hideLoader()

                }
                .addOnSuccessListener {

                    goToMainActivity()

                }
                .addOnFailureListener { exception ->

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "linkWithCredential:failure", exception)

                    val message = when(exception) {

                        is FirebaseAuthUserCollisionException -> "This credential is already associated with a different user account"
                        else -> "Failed to link account with Google"

                    }

                    mGoogleSignInClient.signOut()

                    Snackbar.make(
                        view!!,
                        message,
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
        }

    }

    private fun showLoader() {

        super.showLoader(view!!.spinningProgressBar, view!!.mainLayout, view!!.signupButton)

    }

    private fun hideLoader() {

        super.hideLoader(view!!.spinningProgressBar, view!!.mainLayout, view!!.signupButton)

    }


    private fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)

        showLoader()

    }


    companion object {

        private fun validatePasswordString(password: String): Int {

            if (password.isEmpty()) {

                return ERROR_EMPTY

            }

            if (password.length < 6) {

                return ERROR_TOO_SHORT

            }

            if (password.length > 25) {

                return ERROR_TOO_LONG

            }

            return RESULT_VALID

        }


        private fun validateEmailString(email: String): Int {


            if (email.isEmpty()) {

                return ERROR_EMPTY

            }

            if (!isEmailValid(email)) {

                return ERROR_INVALID

            }

            return RESULT_VALID

        }


        private fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }
    }

}