package com.jdevs.timeo.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.NETWORK_ERROR
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SigninFragBinding
import com.jdevs.timeo.ui.profile.viewmodel.SignInViewModel
import com.jdevs.timeo.util.RequestCodes.RC_SIGN_IN
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.isValidEmail
import com.jdevs.timeo.util.navigateToGraph
import com.jdevs.timeo.util.observeEvent

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private val googleSignInClient by lazy {

        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context!!, googleSignInOptions)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = SigninFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        observeEvent(viewModel.hideKeyboard) {

            hideKeyboard()
        }

        observeEvent(viewModel.signIn) {

            it!!
            signIn(it.first, it.second)
        }

        observeEvent(viewModel.showGoogleSignInIntent) {

            showGoogleSignInIntent()
        }

        observeEvent(viewModel.navigateToSignUp) {

            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root
    }

    private fun signIn(email: String, password: String) {

        when {

            email.isEmpty() -> viewModel.setEmailError(getString(R.string.email_empty))
            !email.isValidEmail() -> viewModel.setEmailError(getString(R.string.email_invalid))
            password.isEmpty() -> viewModel.setPasswordError(getString(R.string.password_empty))

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

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

        return
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
                    SIGN_IN_CANCELLED -> {

                        Log.i(TAG, "Sign in was cancelled by user")
                    }

                    NETWORK_ERROR -> {

                        Snackbar
                            .make(
                                view!!, getString(R.string.check_connection),
                                Snackbar.LENGTH_LONG
                            ).show()
                    }

                    else -> {

                        Log.w(TAG, "Google sign in failed", e)
                        Snackbar.make(
                            view!!,
                            getString(R.string.google_sign_in_failed),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun linkGoogleAccount(account: GoogleSignInAccount) {

        viewModel.signInWithGoogle(account, ::onGoogleSignInFailed) {

            navigateToOverview()
        }
    }

    private fun onGoogleSignInFailed(exception: Exception) {

        Log.w(TAG, "Google sign in failed", exception)
        Snackbar.make(view!!, getString(R.string.google_sign_in_failed), Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun handleException(exception: FirebaseException?) = when (exception) {

        is FirebaseAuthInvalidCredentialsException -> {

            viewModel.setPasswordError(getString(R.string.password_incorrect))
        }

        is FirebaseAuthInvalidUserException -> {

            viewModel.setEmailError(getString(R.string.user_does_not_exist))
        }

        is FirebaseNetworkException -> {

            Snackbar.make(view!!, getString(R.string.check_connection), Snackbar.LENGTH_LONG).show()
        }

        else -> {

            Log.w(TAG, "Failed to sign in", exception)
            Snackbar.make(view!!, getString(R.string.sign_in_failed), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun navigateToOverview() {

        viewModel.hideContent()
        navigateToGraph(R.id.overview)
    }
}
