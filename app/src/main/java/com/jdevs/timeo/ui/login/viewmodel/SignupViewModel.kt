package com.jdevs.timeo.ui.login.viewmodel

import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.util.launchSuspendingProcess

class SignupViewModel : AuthViewModel() {

    var navigator: Navigator? = null

    fun createAccount(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess, navigator) {

            authRepository.createAccount(email, password)
        }
    }

    fun triggerSignUp() {
        navigator?.signUp(email.value.orEmpty(), password.value.orEmpty())
    }

    override fun onFragmentDestroyed() {
        navigator = null
    }

    interface Navigator : LoaderViewModel.Navigator {
        fun navigateToLogin()
        fun signUp(email: String, password: String)
    }
}
