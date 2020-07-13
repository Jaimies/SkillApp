package com.jdevs.timeo.ui.auth

import android.content.Context
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jdevs.timeo.R
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.usecase.auth.SignInUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.util.hardware.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext

class SignInViewModel @ViewModelInject constructor(
    private val signInUseCase: SignInUseCase,
    private val networkUtils: NetworkUtils,
    @ApplicationContext context: Context
) : AuthViewModel() {

    private val googleSignInIntent by lazy {

        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, googleSignInOptions).signInIntent
    }

    val showGoogleSignInIntent = SingleLiveEvent<Intent>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signIn() {
        val email = email.value.orEmpty()
        val password = password.value.orEmpty()

        when {
            !(checkEmail(email) and checkPassword(password)) -> return
            !networkUtils.hasNetworkConnection() -> _snackbar.value = R.string.check_connection
            else -> launchSuspendingProcess(
                ::onSignInResult,
                { result -> result == SignInResult.Success }) {

                signInUseCase.signIn(email, password)
            }
        }
    }

    private fun checkPassword(password: String): Boolean {

        if (password.isEmpty()) {
            passwordError.value = R.string.enter_password
            return false
        }

        passwordError.value = -1
        return true
    }

    fun onSignInCompleted(signInIntent: Intent?) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)

        launchSuspendingProcess(::onGoogleSignInResult, { it == GoogleSignInResult.Success }) {
            signInUseCase.signInWithGoogle(task)
        }
    }

    private fun onSignInResult(result: SignInResult) = when (result) {
        SignInResult.Success -> _navigateToOverview.call()
        SignInResult.NoSuchUser -> emailError.value = R.string.user_does_not_exist
        SignInResult.IncorrectPassword -> passwordError.value = R.string.password_incorrect
        SignInResult.InternalError -> _snackbar.value = R.string.try_again
    }

    private fun onGoogleSignInResult(result: GoogleSignInResult) = when (result) {
        GoogleSignInResult.Success -> _navigateToOverview.call()
        GoogleSignInResult.UserAccountDisabled -> _snackbar.value = R.string.user_account_disabled
        GoogleSignInResult.NetworkFailure -> _snackbar.value = R.string.check_connection
        GoogleSignInResult.InternalError -> _snackbar.value = R.string.try_again
        GoogleSignInResult.Cancelled -> Unit
    }

    fun showGoogleSignIn() {
        showGoogleSignInIntent.value = googleSignInIntent
    }

    fun navigateToSignUp() = navigateToSignUp.call()
}
