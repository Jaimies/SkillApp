package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.databinding.FragmentSignupBinding
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.validateEmail
import com.jdevs.timeo.util.validatePassword
import com.jdevs.timeo.viewmodel.SignupViewModel

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

        viewModel.createAccount(email, password, ::handleException) {

            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
        }
    }

    private fun validatePassword(password: String): Boolean {

        val error = when (password.validatePassword()) {
            R.id.ERROR_EMPTY -> getString(R.string.password_empty)
            R.id.ERROR_TOO_SHORT -> getString(R.string.password_too_short)
            R.id.ERROR_TOO_LONG -> getString(R.string.password_too_long)
            else -> return true
        }

        viewModel.setPasswordError(error)

        return false
    }

    private fun validateEmail(email: String): Boolean {

        val error = when (email.validateEmail()) {
            R.id.ERROR_EMPTY -> getString(R.string.email_empty)
            R.id.ERROR_INVALID -> getString(R.string.email_invalid)
            else -> return true
        }

        viewModel.setEmailError(error)

        return false
    }

    private fun handleException(exception: Exception?) {

        when (exception) {
            is FirebaseAuthWeakPasswordException -> {

                viewModel.setPasswordError(getString(R.string.password_too_weak))
            }

            is FirebaseAuthUserCollisionException -> {

                viewModel.setEmailError(getString(R.string.user_already_exists))
            }

            is FirebaseAuthInvalidCredentialsException -> {

                viewModel.setEmailError(getString(R.string.email_invalid))
            }

            else -> {

                Log.w(TAG, "Failed to sign up", exception)
                Snackbar.make(view!!, getString(R.string.sign_up_failed), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
