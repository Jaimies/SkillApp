package com.jdevs.timeo.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SignupFragBinding
import com.jdevs.timeo.ui.profile.viewmodel.SignUpViewModel
import com.jdevs.timeo.util.EMPTY
import com.jdevs.timeo.util.INVALID
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.TOO_LONG
import com.jdevs.timeo.util.TOO_SHORT
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.navigateToGraph
import com.jdevs.timeo.util.validateEmail
import com.jdevs.timeo.util.validatePassword

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = SignupFragBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this
        }

        viewModel.hideKeyboard.observeEvent(viewLifecycleOwner) {

            hideKeyboard()
        }

        viewModel.navigateToSignIn.observeEvent(viewLifecycleOwner) {

            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        viewModel.signUp.observeEvent(viewLifecycleOwner) {

            it!!
            signUp(it.first, it.second)
        }

        return binding.root
    }

    private fun signUp(email: String, password: String) {

        if (!(validateEmail(email) && validatePassword(password))) {

            return
        }

        viewModel.createAccount(email, password, ::handleException) {

            navigateToGraph(R.id.overview)
        }
    }

    private fun validatePassword(password: String): Boolean {

        val error = when (password.validatePassword()) {

            EMPTY -> getString(R.string.password_empty)
            TOO_SHORT -> getString(R.string.password_too_short)
            TOO_LONG -> getString(R.string.password_too_long)
            else -> return true
        }

        viewModel.setPasswordError(error)

        return false
    }

    private fun validateEmail(email: String): Boolean {

        val error = when (email.validateEmail()) {

            EMPTY -> getString(R.string.email_empty)
            INVALID -> getString(R.string.email_invalid)
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
