package com.jdevs.timeo.ui.auth

import android.content.Context
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jdevs.timeo.R.string.check_connection
import com.jdevs.timeo.R.string.default_web_client_id
import com.jdevs.timeo.R.string.enter_password
import com.jdevs.timeo.R.string.password_incorrect
import com.jdevs.timeo.R.string.try_again
import com.jdevs.timeo.R.string.user_account_disabled
import com.jdevs.timeo.R.string.user_does_not_exist
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
            .requestIdToken(context.getString(default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, googleSignInOptions).signInIntent
    }

    val showGoogleSignInIntent = SingleLiveEvent<Intent>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signIn() {
        performSignIn(email.value.orEmpty(), password.value.orEmpty())
    }

    private fun performSignIn(email: String, password: String) {
        if (!(checkEmail(email) and checkPassword(password))) return
        if (!networkUtils.hasNetworkConnection()) {
            snackbar(check_connection)
            return
        }

        runWithLoader {
            val result = signInUseCase.signIn(email, password)
            onSignInResult(result)
            result == SignInResult.Success
        }
    }

    private fun checkPassword(password: String): Boolean {

        if (password.isEmpty()) {
            setPasswordError(enter_password)
            return false
        }

        setPasswordError(-1)
        return true
    }

    fun onGoogleSignInCompleted(signInIntent: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)

        runWithLoader {
            val result = signInUseCase.signInWithGoogle(task)
            onGoogleSignInResult(result)
            result == GoogleSignInResult.Success
        }
    }

    private fun onSignInResult(result: SignInResult) = when (result) {
        SignInResult.Success -> navigateToOverview()
        SignInResult.NoSuchUser -> setEmailError(user_does_not_exist)
        SignInResult.IncorrectPassword -> setPasswordError(password_incorrect)
        SignInResult.InternalError -> snackbar(try_again)
    }

    private fun onGoogleSignInResult(result: GoogleSignInResult) {
        when (result) {
            GoogleSignInResult.Success -> navigateToOverview()
            GoogleSignInResult.UserAccountDisabled -> snackbar(
                user_account_disabled
            )
            GoogleSignInResult.NetworkFailure -> snackbar(check_connection)
            GoogleSignInResult.InternalError -> snackbar(try_again)
        }
    }

    fun showGoogleSignIn() {
        showGoogleSignInIntent.value = googleSignInIntent
    }

    fun navigateToSignUp() = navigateToSignUp.call()
}
