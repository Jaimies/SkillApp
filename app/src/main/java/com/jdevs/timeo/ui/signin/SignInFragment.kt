package com.jdevs.timeo.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.NETWORK_ERROR
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SigninFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.shared.util.TAG
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hasNetworkConnection
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.isValidEmail
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

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.signIn) { signIn() }
        observe(viewModel.showGoogleSignInIntent) { showGoogleSignInIntent() }

        observe(viewModel.navigateToSignUp) {

            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun signIn() {

        val email = viewModel.email.value.orEmpty()
        val password = viewModel.password.value.orEmpty()

        when {

            email.isEmpty() -> setEmailError(R.string.email_empty)
            !isValidEmail(email) -> setEmailError(R.string.email_invalid)
            password.isEmpty() -> setPasswordError(R.string.password_empty)

            else -> {

                if (!requireContext().hasNetworkConnection) {
                    snackbar(R.string.check_connection)
                    return
                }

                viewModel.signIn(email, password, ::handleException) {
                    navigateToOverview()
                }
            }
        }
    }

    private fun showGoogleSignInIntent() {

        hideKeyboard()
        viewModel.showLoader()
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        viewModel.hideLoader()

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java) ?: return
                linkGoogleAccount(account)
            } catch (e: ApiException) {

                when (e.statusCode) {

                    SIGN_IN_CANCELLED -> Log.d(TAG, "Sign in was cancelled by user")
                    NETWORK_ERROR -> snackbar(R.string.check_connection)

                    else -> {

                        Log.w(TAG, "Google sign in failed", e)
                        snackbar(R.string.try_again)
                    }
                }
            }
        }
    }

    private fun linkGoogleAccount(account: GoogleSignInAccount) {

        viewModel.signInWithGoogle(account, ::onGoogleSignInFailed, ::navigateToOverview)
    }

    private fun onGoogleSignInFailed(exception: Exception) {

        Log.w(TAG, "Google sign in failed", exception)
        snackbar(R.string.try_again)
    }

    private fun handleException(exception: Exception) {

        when (exception) {

            is FirebaseAuthInvalidCredentialsException -> {

                setPasswordError(R.string.password_incorrect)
            }

            is FirebaseAuthInvalidUserException -> {

                setEmailError(R.string.user_does_not_exist)
            }

            is FirebaseNetworkException -> {

                snackbar(R.string.check_connection)
            }

            else -> {

                Log.w(TAG, "Sign in failed", exception)
                snackbar(R.string.try_again)
            }
        }
    }
}
