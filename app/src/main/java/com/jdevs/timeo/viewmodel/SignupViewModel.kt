package com.jdevs.timeo.viewmodel

import com.jdevs.timeo.viewmodel.common.AuthViewModel

class SignupViewModel : AuthViewModel() {

    var navigator: Navigator? = null

    fun createAccount(
        email: String,
        password: String,
        failure: (Exception) -> Unit,
        success: () -> Unit
    ) {

        launchSuspendingProcess(failure, success, navigator) {

            authRepository.createAccount(email, password)
        }
    }

    fun triggerSignUp() {
        navigator?.signUp(email.value.orEmpty(), password.value.orEmpty())
    }

    interface Navigator : AuthViewModel.Navigator {
        fun navigateToLogin()
        fun signUp(email: String, password: String)
    }
}
