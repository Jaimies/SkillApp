package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class LoginViewModel : AuthViewModel() {

    private val _emailError = MutableLiveData("")
    private val _passwordError = MutableLiveData("")

    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>

    var navigator: Navigator? = null

    fun setEmailError(error: String) {
        _passwordError.value = ""
        _emailError.value = error
    }

    fun setPasswordError(error: String) {
        _emailError.value = ""
        _passwordError.value = error
    }

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        failure: (Exception) -> Unit,
        success: () -> Unit
    ) {
        launchSuspendingProcess(failure, success, navigator) {
            authRepository.linkGoogleAccount(account)
        }
    }

    fun signIn(email: String, password: String, failure: (Exception) -> Unit, success: () -> Unit) {

        launchSuspendingProcess(failure, success, navigator) {
            authRepository.signIn(email, password)
        }
    }

    interface Navigator : AuthViewModel.Navigator {
        fun navigateToSignup()
        fun signIn(email: String, password: String)
        fun showGoogleSignInIntent()
    }
}
