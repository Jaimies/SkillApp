package com.jdevs.timeo.ui.signin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val authRepository: AuthRepository) :
    AuthViewModel() {

    val signIn = SingleLiveEvent<Any>()
    val showGoogleSignInIntent = SingleLiveEvent<Any>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            authRepository.linkGoogleAccount(account)
        }
    }

    fun signIn(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            authRepository.signIn(email, password)
        }
    }

    fun triggerSignIn() {

        signIn.call()
        hideKeyboard()
    }

    fun showGoogleSignInIntent() {

        showGoogleSignInIntent.call()
        hideKeyboard()
    }

    fun navigateToSignUp() = navigateToSignUp.call()
}
