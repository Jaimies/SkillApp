package com.jdevs.timeo.ui.profile.viewmodel

import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.launchSuspendingProcess

class SignUpViewModel : AuthViewModel() {

    val signUp = SingleLiveEvent<Pair<String, String>>()
    val navigateToSignIn = SingleLiveEvent<Any>()

    fun createAccount(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            AuthRepository.createAccount(email, password)
        }
    }

    fun triggerSignUp() {

        signUp.value = email.value.orEmpty() to password.value.orEmpty()
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
