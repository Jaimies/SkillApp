package com.jdevs.timeo.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SignupFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.util.EMPTY
import com.jdevs.timeo.util.TOO_LONG
import com.jdevs.timeo.util.TOO_SHORT
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hasNetworkConnection
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

        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.signUp) { signUp() }
        observe(viewModel.navigateToSignIn) {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun signUp() {

        val email = viewModel.email.value.orEmpty()
        val password = viewModel.password.value.orEmpty()

        when {
            !(checkEmail(email) and checkPassword(password)) -> return
            !requireContext().hasNetworkConnection -> snackbar(R.string.check_connection)

            else -> viewModel.createAccount(email, password, ::onSignUpResult)
        }
    }

    private fun checkPassword(password: String): Boolean {

        passwordError = when (validatePassword(password)) {
            EMPTY -> R.string.password_empty
            TOO_SHORT -> R.string.password_too_short
            TOO_LONG -> R.string.password_too_long
            else -> {
                viewModel.passwordError.value = ""
                return true
            }
        }

        return false
    }

    private fun onSignUpResult(result: SignUpResult) = when (result) {
        SignUpResult.Success -> navigateToOverview()
        SignUpResult.InvalidEmail -> emailError = R.string.email_invalid
        SignUpResult.WeakPassword -> passwordError = R.string.password_too_weak
        SignUpResult.UserAlreadyExists -> emailError = R.string.user_already_exists
        SignUpResult.InternalError -> snackbar(R.string.try_again)
    }
}
