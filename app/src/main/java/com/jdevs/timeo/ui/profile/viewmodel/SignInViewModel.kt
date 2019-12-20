package com.jdevs.timeo.ui.profile.viewmodel

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.launchSuspendingProcess

class SignInViewModel : AuthViewModel() {

    val signIn = SingleLiveEvent<Pair<String, String>>()
    val showGoogleSignInIntent = SingleLiveEvent<Any>()
    val navigateToSignUp = SingleLiveEvent<Any>()

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onFailure: (FirebaseException) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            AuthRepository.linkGoogleAccount(account)
        }
    }

    fun signIn(
        email: String,
        password: String,
        onFailure: (FirebaseException) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            AuthRepository.signIn(email, password)
        }
    }

    fun triggerSignIn() {

        signIn.value = email.value.orEmpty() to password.value.orEmpty()
    }

    fun showGoogleSignInIntent() {

        showGoogleSignInIntent.call()
    }

    fun navigateToSignUp() {

        navigateToSignUp.call()
    }
}
