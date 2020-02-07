package com.jdevs.timeo.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.NETWORK_ERROR
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SigninFragBinding
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.extensions.appComponent
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.showSnackbar
import com.jdevs.timeo.util.hideKeyboard
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

    @Inject
    override lateinit var viewModel: SignInViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SigninFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        observeEvent(viewModel.hideKeyboard) { hideKeyboard() }
        observeEvent(viewModel.signIn) { signIn(it!!.first, it.second) }
        observeEvent(viewModel.showGoogleSignInIntent) { showGoogleSignInIntent() }

        observeEvent(viewModel.navigateToSignUp) {

            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root
    }

    private fun signIn(email: String, password: String) {

        when {

            email.isEmpty() -> setEmailError(R.string.email_empty)
            !isValidEmail(email) -> setEmailError(R.string.email_invalid)
            password.isEmpty() -> setPasswordError(R.string.password_empty)

            else -> {

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

                    SIGN_IN_CANCELLED -> Log.i(TAG, "Sign in was cancelled by user")
                    NETWORK_ERROR -> showSnackbar(R.string.check_connection)

                    else -> {

                        Log.w(TAG, "Google sign in failed", e)
                        showSnackbar(R.string.google_sign_in_failed)
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
        showSnackbar(R.string.google_sign_in_failed)
    }

    private fun handleException(exception: Exception) = when (exception as? FirebaseException) {

        is FirebaseAuthInvalidCredentialsException -> {

            setPasswordError(R.string.password_incorrect)
        }

        is FirebaseAuthInvalidUserException -> {

            setPasswordError(R.string.user_does_not_exist)
        }

        is FirebaseNetworkException -> {

            showSnackbar(R.string.check_connection)
        }

        else -> {

            Log.w(TAG, "Failed to sign in", exception)
            showSnackbar(R.string.sign_in_failed)
        }
    }
}
