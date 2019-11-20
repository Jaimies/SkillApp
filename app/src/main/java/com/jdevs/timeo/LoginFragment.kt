package com.jdevs.timeo

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
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.models.AuthFragment
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import kotlinx.android.synthetic.main.circular_loader.view.spinningProgressBar
import kotlinx.android.synthetic.main.fragment_login.view.emailEditText
import kotlinx.android.synthetic.main.fragment_login.view.emailTextInputLayout
import kotlinx.android.synthetic.main.fragment_login.view.googleSignInButton
import kotlinx.android.synthetic.main.fragment_login.view.loginButton
import kotlinx.android.synthetic.main.fragment_login.view.mainLayout
import kotlinx.android.synthetic.main.fragment_login.view.passwordEditText
import kotlinx.android.synthetic.main.fragment_login.view.passwordTextInputLayout
import kotlinx.android.synthetic.main.fragment_login.view.signupTextView

class LoginFragment : AuthFragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var mEmailEditText: EditText
    private lateinit var mEmailTextInputLayout: TextInputLayout
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordTextInputLayout: TextInputLayout

    private lateinit var mLoginButton: Button
    private lateinit var mMainLayout: LinearLayout

    private lateinit var mLoader: FrameLayout

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            hideLoader()

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java) ?: return

                firebaseLinkGoogleAccount(account)
            } catch (e: ApiException) {

                if (e.statusCode == SIGN_IN_CANCELLED) {

                    Log.i(TAG, "Sign in was cancelled by user")

                    return
                }

                Log.w(TAG, "Google sign in failed", e)
                Snackbar.make(view!!, "Failed to sign in with Google", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.apply {

            mEmailEditText = emailEditText

            mEmailTextInputLayout = emailTextInputLayout

            mPasswordEditText = passwordEditText.apply {

                setOnKeyListener(this@LoginFragment)
            }

            mPasswordTextInputLayout = passwordTextInputLayout

            mLoginButton = loginButton.apply {

                setOnClickListener(this@LoginFragment)
            }

            mLoader = spinningProgressBar
            mMainLayout = mainLayout

            signupTextView.setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }

        makeTextViewClickable(view.signupTextView)

        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOptions)

        mGoogleSignInClient.signOut()

        view.rootView.setOnClickListener {

            hideKeyboard(activity)
        }

        view.googleSignInButton.setOnClickListener {

            showLoader()

            showGoogleSignInIntent()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onClick(v: View?) {

        val email = mEmailEditText.text.toString()
        val password = mPasswordEditText.text.toString()

        if (email.isEmpty()) {

            setError(mEmailTextInputLayout, mEmailEditText, "Email must not be empty")

            return
        }

        if (!isEmailValid(email)) {

            setError(mEmailTextInputLayout, mEmailEditText, "Email is invalid", true)

            return
        }

        if (password.isEmpty()) {

            setError(
                mPasswordTextInputLayout,
                mPasswordEditText,
                "Password must not be empty"
            )

            return
        }

        signIn(email, password)
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

            is FirebaseAuthInvalidCredentialsException -> {

                setError(
                    mPasswordTextInputLayout,
                    mPasswordEditText,
                    "Username and password do not match"
                )
            }

            is FirebaseAuthInvalidUserException -> {

                setError(
                    mEmailTextInputLayout,
                    mEmailEditText,
                    "User with that email does not exist"
                )
            }

            is FirebaseNetworkException -> {

                Snackbar.make(
                    view!!,
                    "Could not sign in, please check your Internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            else -> {

                Log.w(TAG, "Failed to sign in", task.exception)

                Snackbar.make(view!!, "Failed to sign in", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if ((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) &&
            event.action == KeyEvent.ACTION_DOWN
        ) {

            onClick(v)
        }

        return false
    }

    private fun firebaseLinkGoogleAccount(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        showLoader()

        val user = mAuth.currentUser

        if (user != null && user.isAnonymous) {

            user.linkWithCredential(credential)
                .addOnCompleteListener {

                    hideLoader()
                }
                .addOnSuccessListener {

                    goToMainActivity()
                }
                .addOnFailureListener { exception ->

                    if (exception is FirebaseAuthUserCollisionException) {

                        firebaseAuthWithGoogle(credential)

                        return@addOnFailureListener
                    }

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", exception)
                    Snackbar.make(
                        view!!,
                        "Authentication Failed",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
        } else {

            firebaseAuthWithGoogle(credential)
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {

        showLoader()

        mAuth.currentUser!!.delete().addOnFailureListener { exception ->

            Log.w(TAG, "Failed to delete anonymous user", exception)
        }

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {

                hideLoader()
            }
            .addOnSuccessListener {

                goToMainActivity()
            }
            .addOnFailureListener { exception ->

                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", exception)
                Snackbar.make(
                    view!!,
                    "Authentication Failed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    private fun goToMainActivity() {

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun setError(
        inputLayout: TextInputLayout,
        editText: EditText,
        error: String,
        needsEmailValidation: Boolean = false
    ) {

        if (inputLayout != mEmailTextInputLayout) {

            removeErrorMessage(mEmailTextInputLayout)
        }

        if (inputLayout != mPasswordTextInputLayout) {

            removeErrorMessage(mPasswordTextInputLayout)
        }

        inputLayout.error = error

        editText.apply {

            requestFocus()
            setSelection(this.length())

            addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    if (s.isNotEmpty() && (needsEmailValidation && isEmailValid(s))) {

                        removeErrorMessage(this@LoginFragment.mEmailTextInputLayout)

                        mEmailEditText.removeTextChangedListener(this)
                    }
                }
            })
        }
    }

    private fun removeErrorMessage(inputLayout: TextInputLayout) {

        inputLayout.apply {

            error = ""

            isErrorEnabled = false
        }
    }

    private fun signIn(email: String, password: String) {

        mAuth.currentUser?.delete()?.addOnFailureListener { exception ->

            Log.w(TAG, "Failed to delete user data", exception)
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)

        showLoader()
    }

    private fun showLoader() {

        super.showLoader(mLoader, mMainLayout, mLoginButton)
    }

    private fun hideLoader() {

        super.hideLoader(mLoader, mMainLayout, mLoginButton)
    }

    private fun showGoogleSignInIntent() {

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}
