package com.jdevs.timeo.ui.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SignupFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.shared.util.TAG
import com.jdevs.timeo.util.EMPTY
import com.jdevs.timeo.util.INVALID
import com.jdevs.timeo.util.TOO_LONG
import com.jdevs.timeo.util.TOO_SHORT
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hasNetworkConnection
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.validateEmail
import com.jdevs.timeo.util.validatePassword
import javax.inject.Inject

class SignUpFragment : AuthFragment() {

    override val viewModel: SignUpViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SignupFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.signUp) { signUp() }

        observe(viewModel.navigateToSignIn) {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun signUp() {

        val email = viewModel.email.value.orEmpty()
        val password = viewModel.password.value.orEmpty()

        if (!(checkEmail(email) && checkPassword(password))) {

            return
        }

        if (!requireContext().hasNetworkConnection) {

            snackbar(R.string.check_connection)
            return
        }

        viewModel.createAccount(email, password, ::handleException, ::navigateToOverview)
    }

    private fun checkPassword(password: String): Boolean {

        passwordError = when (validatePassword(password)) {

            EMPTY -> R.string.password_empty
            TOO_SHORT -> R.string.password_too_short
            TOO_LONG -> R.string.password_too_long
            else -> return true
        }

        return false
    }

    private fun checkEmail(email: String): Boolean {

        emailError = when (validateEmail(email)) {

            EMPTY -> R.string.email_empty
            INVALID -> R.string.email_invalid
            else -> return true
        }

        return false
    }

    private fun handleException(exception: Exception?) {

        when (exception) {

            is FirebaseAuthWeakPasswordException -> passwordError = R.string.password_too_weak
            is FirebaseAuthUserCollisionException -> emailError = R.string.user_already_exists
            is FirebaseAuthInvalidCredentialsException -> emailError = R.string.email_invalid

            else -> {

                Log.w(TAG, "Failed to sign up", exception)
                snackbar(R.string.try_again)
            }
        }
    }
}
