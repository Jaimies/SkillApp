package com.jdevs.timeo


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
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

        super.onCreateView(inflater, container, savedInstanceState)


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
                requireActivity().toolbar.navigationIcon = null

            }

            makeTextViewClickable(this)
        }



        view.rootView.apply {

            setOnClickListener {

                hideKeyboard(activity)

            }

        }

        // Inflate the layout for this fragment
        return view
    }





    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user != null) {

            returnToMainActivity()

        }

    }

    override fun onClick(view: View?) {

        if(!validateInput(emailTextInputLayout, emailEditText, ::validateEmailString, ::showEmailError)) {

            return

        }

        if(!validateInput(passwordTextInputLayout, passwordEditText, ::validatePasswordString, ::showPasswordError)) {

            return

        }

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        signUp(email, password)

    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        if((keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.action == KeyEvent.ACTION_DOWN) {

            onClick(v)

        }

        return false

    }


    override fun onComplete(task: Task<AuthResult>) {

        hideLoader()

        if(task.isSuccessful) {

            goToMainActivity()

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




    private fun validateInput(
        textInputLayout: TextInputLayout,
        editText: EditText,
        validator: (String) -> Int,
        errorHandler : (Int) -> Unit
    ) : Boolean{

        val value = editText.text.toString()

        val validationResult = validator(value)

        if(validationResult != RESULT_VALID) {

            errorHandler(validationResult)

            editText.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}


                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if(validator(s.toString()) != validationResult) {

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

    private fun showLoader() {

        super.showLoader(spinningProgressBar, mainLayout, signupButton)

    }

    private fun hideLoader() {

        super.hideLoader(spinningProgressBar, mainLayout, signupButton)

    }


    private fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this)

        showLoader()

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