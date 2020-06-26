package com.jdevs.timeo.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SigninFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hasNetworkConnection
import javax.inject.Inject

private const val RC_SIGN_IN = 0

class SignInFragment : AuthFragment() {

    private val googleSignInClient by lazy {

        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    override val viewModel: SignInViewModel by viewModels { viewModelFactory }

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

        val binding = SigninFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.signIn) { signIn() }
        observe(viewModel.showGoogleSignInIntent) {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }

        observe(viewModel.navigateToSignUp) {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun signIn() {

        val email = viewModel.email.value.orEmpty()
        val password = viewModel.password.value.orEmpty()

        when {
            !(checkEmail(email) and checkPassword(password)) -> return
            !requireContext().hasNetworkConnection -> snackbar(R.string.check_connection)
            else -> viewModel.signIn(email, password, ::onSignInResult)
        }
    }

    private fun checkPassword(password: String): Boolean {

        if (password.isEmpty()) {
            passwordError = R.string.password_empty
            return false
        }

//        viewModel.passwordError.value = ""
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (requestCode != RC_SIGN_IN) return

        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        viewModel.signInWithGoogle(task, ::onGoogleSignInResult)
    }

    private fun onSignInResult(result: SignInResult) = when (result) {
        SignInResult.Success -> navigateToOverview()
        SignInResult.NoSuchUser -> emailError = R.string.user_does_not_exist
        SignInResult.IncorrectPassword -> passwordError = R.string.password_incorrect
        SignInResult.InternalError -> snackbar(R.string.try_again)
    }

    private fun onGoogleSignInResult(result: GoogleSignInResult) = when (result) {
        GoogleSignInResult.Success -> navigateToOverview()
        GoogleSignInResult.UserAccountDisabled -> snackbar(R.string.user_account_disabled)
        GoogleSignInResult.NetworkFailure -> snackbar(R.string.check_connection)
        GoogleSignInResult.InternalError -> snackbar(R.string.try_again)
        GoogleSignInResult.Cancelled -> Unit
    }
}
