package com.jdevs.timeo


import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*


const val TAG = "Login"

const val ERROR_EMPTY = 1220
const val ERROR_INVALID = 1221

const val ERROR_TOO_SHORT = 1223
const val ERROR_TOO_LONG = 1224

const val RESULT_VALID = 1222


class LoginFragment : Fragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordTextInputLayout : TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var loginButton: Button
    private lateinit var signupTextView : TextView

    private lateinit var spinningProgressBar : FrameLayout

    private lateinit var mainLayout : LinearLayout


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

                hideKeyboard(activity!!)

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

        spinningProgressBar.apply {

            visibility = View.INVISIBLE

            alpha = 0.0f

        }

        mainLayout.apply {

            alpha  = 1.0f

        }

        loginButton.apply {

            isEnabled = true

        }


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

                Toast.makeText(context, "Failed to sign in", Toast.LENGTH_LONG).show()

            }

        }


    }


    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.action == KeyEvent.ACTION_DOWN) {

            onClick(v)

        }

        return false

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

        spinningProgressBar.apply{

            visibility = View.VISIBLE

            alpha = 1.0f

        }

        mainLayout.apply {

            alpha  = 0.5f

        }

        loginButton.apply {

            isEnabled = false

        }

        if(activity != null){

            hideKeyboard(activity!!)

        }

    }


    companion object {

        private fun isEmailValid(email: CharSequence): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }

        private fun hideKeyboard(activity : FragmentActivity) {

            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus

            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {

                view = View(activity)

            }

            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        }

    }
}
