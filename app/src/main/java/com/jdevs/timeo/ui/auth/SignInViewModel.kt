package com.jdevs.timeo.ui.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jdevs.timeo.domain.usecase.auth.SignInUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase) :
    AuthViewModel() {

    val signIn = SingleLiveEvent<Any>()
    val showGoogleSignInIntent = SingleLiveEvent<Any>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signIn(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            signInUseCase.signIn(email, password)
        }
    }

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            signInUseCase.signInWithGoogle(account)
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
