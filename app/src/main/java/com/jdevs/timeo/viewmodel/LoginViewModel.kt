package com.jdevs.timeo.viewmodel

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.jdevs.timeo.viewmodel.common.AuthViewModel

class LoginViewModel : AuthViewModel() {

    var navigator: Navigator? = null

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        failure: (FirebaseException) -> Unit,
        success: () -> Unit
    ) {

        launchSuspendingProcess(failure, success, navigator) {

            authRepository.linkGoogleAccount(account)
        }
    }

    fun signIn(
        email: String,
        password: String,
        failure: (FirebaseException) -> Unit,
        success: () -> Unit
    ) {

        launchSuspendingProcess(failure, success, navigator) {

            authRepository.signIn(email, password)
        }
    }

    fun triggerSignIn() {

        navigator?.signIn(email.value.orEmpty(), password.value.orEmpty())
    }

    interface Navigator : AuthViewModel.Navigator {
        fun navigateToSignup()
        fun signIn(email: String, password: String)
        fun showGoogleSignInIntent()
    }
}
