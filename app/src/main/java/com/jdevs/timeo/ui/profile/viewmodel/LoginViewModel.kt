package com.jdevs.timeo.ui.profile.viewmodel

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.util.launchSuspendingProcess

class LoginViewModel : AuthViewModel() {

    var navigator: Navigator? = null

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onFailure: (FirebaseException) -> Unit,
        onSuccess: () -> Unit
    ) {

        launchSuspendingProcess(onFailure, onSuccess, navigator) {

            authRepository.linkGoogleAccount(account)
        }
    }

    fun signIn(
        email: String,
        password: String,
        onFailure: (FirebaseException) -> Unit,
        onSuccess: () -> Unit
    ) {

        launchSuspendingProcess(onFailure, onSuccess, navigator) {

            authRepository.signIn(email, password)
        }
    }

    fun triggerSignIn() {

        navigator?.signIn(email.value.orEmpty(), password.value.orEmpty())
    }

    override fun onFragmentDestroyed() {
        navigator = null
    }

    interface Navigator : LoaderViewModel.Navigator {
        fun navigateToSignup()
        fun signIn(email: String, password: String)
        fun showGoogleSignInIntent()
    }
}
