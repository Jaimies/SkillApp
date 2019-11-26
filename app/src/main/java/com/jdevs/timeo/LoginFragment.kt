package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jdevs.timeo.databinding.FragmentLoginBinding
import com.jdevs.timeo.util.RC_SIGN_IN
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.isValidEmail
import com.jdevs.timeo.viewmodels.LoginViewModel

class LoginFragment : Fragment(),
    LoginViewModel.Navigator {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java).also {
            it.navigator = this
        }
    }

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
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this@LoginFragment
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }

    override fun hideKeyboard() {
        hideKeyboard(activity)
    }

    override fun signIn(email: String, password: String) {
        when {
            email.isEmpty() -> viewModel.setEmailError("Email must not be empty")
            !email.isValidEmail() -> viewModel.setEmailError("Email is invalid")
            password.isEmpty() -> viewModel.setPasswordError("Password must not be empty")

            else -> {

                viewModel.signIn(email, password, ::handleException) {

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    override fun showGoogleSignInIntent() {

        hideKeyboard()
        viewModel.showLoader()

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

        return
    }

    override fun navigateToSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        viewModel.hideLoader()

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java) ?: return
                linkGoogleAccount(account)
            } catch (e: ApiException) {
                if (e.statusCode == SIGN_IN_CANCELLED) {
                    Log.i(TAG, "Sign in was cancelled by user")
                    return
                }

                Log.w(TAG, "Google sign in failed", e)
                Snackbar.make(view!!, "Failed to sign in with Google", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun linkGoogleAccount(account: GoogleSignInAccount) {

        viewModel.signInWithGoogle(account, ::onGoogleSignInFailed) {

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun onGoogleSignInFailed(exception: Exception) {

        Log.w(TAG, "Google sign in failed", exception)
        Snackbar.make(view!!, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
    }

    private fun handleException(exception: Exception?) {

        when (exception) {

            is FirebaseAuthInvalidCredentialsException -> {
                viewModel.setPasswordError("Username and password do not match")
            }

            is FirebaseAuthInvalidUserException -> {
                viewModel.setEmailError("User with that email does not exist")
            }

            is FirebaseNetworkException -> {
                Snackbar.make(view!!, "Please check your Internet connection", Snackbar.LENGTH_LONG)
                    .show()
            }

            else -> {
                Log.w(TAG, "Failed to sign in", exception)
                Snackbar.make(view!!, "Failed to sign in", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
