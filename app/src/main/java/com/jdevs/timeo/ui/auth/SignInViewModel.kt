package com.jdevs.timeo.ui.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.usecase.auth.SignInUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase) :
    AuthViewModel() {

    val signIn = SingleLiveEvent<Any>()
    val showGoogleSignInIntent = SingleLiveEvent<Any>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signIn(
        email: String, password: String,
        onResult: (SignInResult) -> Unit
    ) = launchSuspendingProcess(onResult, { result -> result == SignInResult.Success }) {

        signInUseCase.signIn(email, password)
    }

    fun signInWithGoogle(
        signInAccountTask: Task<GoogleSignInAccount>,
        onResult: (GoogleSignInResult) -> Unit
    ) = launchSuspendingProcess(onResult, { result -> result == GoogleSignInResult.Success }) {

        signInUseCase.signInWithGoogle(signInAccountTask)
    }

    fun signIn() = signIn.call()
    fun showGoogleSignIn() = showGoogleSignInIntent.call()
    fun navigateToSignUp() = navigateToSignUp.call()
}
