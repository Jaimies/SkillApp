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
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*

/**
 * A simple [Fragment] subclass.
 */
class SignupFragment : Fragment(),
    View.OnClickListener,
    View.OnKeyListener,
    OnCompleteListener<AuthResult> {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: TextInputEditText

    private lateinit var passwordTextInputLayout : TextInputLayout
    private lateinit var passwordEditText: TextInputEditText

    private lateinit var signupButton: Button
    private lateinit var loginTextView : TextView

    private lateinit var spinningProgressBar : FrameLayout

    private lateinit var mainLayout : LinearLayout



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
        passwordEditText = view.passwordEditText.apply {

            setOnKeyListener(this@SignupFragment)

        }

        signupButton = view.loginButton.apply {

            setOnClickListener(this@SignupFragment)

        }

        loginTextView = view.loginTextView

        mainLayout = view.mainLayout

        spinningProgressBar = view.spinningProgressBar




        loginTextView = view.loginTextView.apply {

            setOnClickListener {

                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

            }

        }

        makeTextViewClickable()


        view.rootView.apply {

            setOnClickListener {

                hideKeyboard(activity!!)

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

    override fun onClick(view: View?) {

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


        signUp(email, password)

    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.action == KeyEvent.ACTION_DOWN) {

            onClick(v)

        }

        return false

    }


    override fun onComplete(task: Task<AuthResult>) {

        spinningProgressBar.apply{

            visibility = View.INVISIBLE

            alpha = 0.0f

        }

        mainLayout.apply {

            alpha  = 1.0f

        }

        signupButton.apply {

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

            is FirebaseAuthWeakPasswordException -> {

                setError(passwordTextInputLayout, passwordEditText, "The password is too weak")

            }

            is FirebaseAuthUserCollisionException -> {

                setError(emailTextInputLayout, emailEditText, "User with that email already exists")

            }

            is FirebaseAuthInvalidCredentialsException -> {

                setError(emailTextInputLayout, emailEditText, "Email is invalid")

            }

            else -> {

                Log.w(TAG, "Failed to sign up", task.exception)

                Toast.makeText(context, "Failed to sign in", Toast.LENGTH_LONG).show()

            }

        }

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

        setError(passwordTextInputLayout, passwordEditText, error)

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

        setError(emailTextInputLayout, emailEditText, error)

    }



    private fun setError(inputLayout: TextInputLayout, editText: EditText, error : String) {

        if(inputLayout != emailTextInputLayout) {

            removeErrorMessage(inputLayout)

        }

        if(inputLayout != passwordTextInputLayout) {

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

    private fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)


        spinningProgressBar.apply{

            visibility = View.VISIBLE

            alpha = 1.0f

        }

        mainLayout.apply {

            alpha  = 0.5f

        }

        signupButton.apply {

            isEnabled = false

        }

        if(activity != null){

            hideKeyboard(activity!!)

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

    private fun makeTextViewClickable() {

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

                    MotionEvent.ACTION_CANCEL -> this@SignupFragment.loginTextView.setText(

                        notClickedString,
                        TextView.BufferType.SPANNABLE

                    )

                }

                true
            }

        }

    }


}
