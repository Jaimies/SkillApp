package com.jdevs.timeo.ui.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SignupFragBinding
import com.jdevs.timeo.util.EMPTY
import com.jdevs.timeo.util.INVALID
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.TOO_LONG
import com.jdevs.timeo.util.TOO_SHORT
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.showSnackbar
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.validateEmail
import com.jdevs.timeo.util.validatePassword
import javax.inject.Inject

class SignUpFragment : AuthFragment() {

    @Inject
    override lateinit var viewModel: SignUpViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SignupFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        observeEvent(viewModel.hideKeyboard) { hideKeyboard() }

        observeEvent(viewModel.navigateToSignIn) {

            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        observeEvent(viewModel.signUp) {

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

            navigateToOverview()
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

                setPasswordError(R.string.password_too_weak)
            }

            is FirebaseAuthUserCollisionException -> {

                setEmailError(R.string.user_already_exists)
            }

            is FirebaseAuthInvalidCredentialsException -> {

                setEmailError(R.string.email_invalid)
            }

            else -> {

                Log.w(TAG, "Failed to sign up", exception)
                showSnackbar(R.string.sign_up_failed)
            }
        }
    }
}
