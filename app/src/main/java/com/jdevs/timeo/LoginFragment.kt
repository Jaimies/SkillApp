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
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.jdevs.timeo.helpers.KeyboardHelper
import com.jdevs.timeo.model.LoaderFragment
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*


const val TAG = "Login"

const val ERROR_EMPTY = 1220
const val ERROR_INVALID = 1221

const val ERROR_TOO_SHORT = 1223
const val ERROR_TOO_LONG = 1224

const val RESULT_VALID = 1222


class LoginFragment : LoaderFragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private lateinit var auth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordTextInputLayout : TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var loginButton: Button
    private lateinit var signupTextView : TextView

    private lateinit var spinningProgressBar : FrameLayout

    private lateinit var mainLayout : LinearLayout


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {

            hideLoader()

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {

                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

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
        passwordEditText = view.passwordEditText.apply {

            setOnKeyListener(this@LoginFragment)

        }

        loginButton = view.loginButton.apply {

            setOnClickListener(this@LoginFragment)

        }


        signupTextView = view.signupTextView.apply {

            setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)

            }
        }

        makeTextViewClickable()


        mainLayout = view.mainLayout

        spinningProgressBar = view.spinningProgressBar


        view.rootView.apply {

            setOnClickListener {

                hideKeyboard(activity)

            }

        }

        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.google_oauth_key))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOptions)

        view.googleSignInButton.apply {

            setOnClickListener {

                showGoogleSignInIntent()

            }

        }


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

            setError(emailTextInputLayout, emailEditText, "Email is invalid", true)

            return

        }

        if(password.isEmpty()) {

            setError(passwordTextInputLayout, passwordEditText,"Password must not be empty")

            return

        }


        signIn(email, password)

    }



    override fun onComplete(task: Task<AuthResult>) {

        hideLoader()


        if(task.isSuccessful) {

            gotoMainActivity()

            return

        }

        if(task.exception == null) {

            return

        }


        when(task.exception) {

            is FirebaseAuthInvalidCredentialsException -> {

                setError(passwordTextInputLayout, passwordEditText, "Username and password do not match")

            }

            is FirebaseAuthInvalidUserException -> {

                setError(emailTextInputLayout, emailEditText, "User with that email does not exist")

            }

            is FirebaseNetworkException -> {

                Toast.makeText(context, "Could not sign in, please check your Internet connection", Toast.LENGTH_LONG).show()

            }

            else -> {

                Log.w(TAG, "Failed to sign in", task.exception)

                Snackbar.make(view!!.rootView, "Failed to sign in", Snackbar.LENGTH_LONG).show()

            }

        }


    }


    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.action == KeyEvent.ACTION_DOWN) {

            onClick(v)

        }

        return false

    }



    private fun showGoogleSignInIntent() {

        showLoader()

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }




    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        showLoader()

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if(task.isSuccessful) {

                    gotoMainActivity()

                } else {

                    hideLoader()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(view!!.rootView, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

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




    private fun setError(inputLayout: TextInputLayout, editText: EditText, error : String, needsEmailValidation: Boolean = false) {

        if(inputLayout != emailTextInputLayout) {

            removeErrorMessage(emailTextInputLayout)

        }

        if(inputLayout != passwordTextInputLayout) {

            removeErrorMessage(passwordTextInputLayout)

        }

        inputLayout.error = error

        editText.apply {

            requestFocus()
            setSelection(this.length())

            addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    if(s.isNotEmpty() && (needsEmailValidation && isEmailValid(s))) {

                        removeErrorMessage(this@LoginFragment.emailTextInputLayout)

                        emailEditText.removeTextChangedListener(this)

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

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)

        showLoader()

    }

    private fun showLoader() {

        super.showLoader(spinningProgressBar, mainLayout, loginButton)

    }

    private fun hideLoader() {

        super.hideLoader(spinningProgressBar, mainLayout, loginButton)

    }


    companion object : KeyboardHelper() {

        const val RC_SIGN_IN = 101

        private fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }

    }
}
