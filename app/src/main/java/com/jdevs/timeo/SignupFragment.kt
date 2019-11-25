package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.databinding.FragmentSignupBinding
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.validateEmail
import com.jdevs.timeo.util.validatePassword
import com.jdevs.timeo.viewmodels.SignupViewModel

class SignupFragment : Fragment(),
    SignupViewModel.Navigator {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SignupViewModel::class.java).also {
            it.navigator = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignupBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.navigator = null
    }

    override fun navigateToLogin() {
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }

    override fun hideKeyboard() {
        hideKeyboard(activity)
    }

    override fun signUp(email: String, password: String) {

        if (!(validateEmail(email) && validatePassword(password))) {

            return
        }

        viewModel.createAccount(email, password).observe(this) { authState ->

            when (authState.state) {

                R.id.AUTH_STATE_FINISHED -> {

                    viewModel.hideLoader()
                }

                R.id.AUTH_STATE_SUCCESSFUL -> {

                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                }

                R.id.AUTH_STATE_FAILED -> {

                    handleException(authState.exception)
                }
            }
        }

        viewModel.showLoader()
        hideKeyboard()
    }

    private fun validatePassword(password: String): Boolean {
        val result = password.validatePassword()

        if (result != R.id.RESULT_VALID) {

            val error = when (result) {
                R.id.RESULT_VALID -> ""
                R.id.ERROR_EMPTY -> "Password must not be empty"
                R.id.ERROR_TOO_SHORT -> "Password must be at least 6 characters long"
                R.id.ERROR_TOO_LONG -> "Password length must not exceed 25 characters"
                else -> "Password is not valid"
            }

            viewModel.setPasswordError(error)

            return false
        }

        return true
    }

    private fun validateEmail(email: String): Boolean {
        val result = email.validateEmail()

        if (result != R.id.RESULT_VALID) {

            val error = when (result) {
                R.id.RESULT_VALID -> ""
                R.id.ERROR_EMPTY -> "Email must not be empty"
                else -> "Email is not valid"
            }

            viewModel.setEmailError(error)

            return false
        }

        return true
    }

    private fun handleException(exception: Exception?) {

        when (exception) {
            is FirebaseAuthWeakPasswordException -> {

                viewModel.setPasswordError("The password is too weak")
            }

            is FirebaseAuthUserCollisionException -> {

                viewModel.setEmailError("User with that email already exists")
            }

            is FirebaseAuthInvalidCredentialsException -> {

                viewModel.setEmailError("Email is invalid")
            }

            else -> {

                Log.w(TAG, "Failed to sign up", exception)
                Toast.makeText(context, "Failed to sign in", Toast.LENGTH_LONG).show()
            }
        }
    }
}
