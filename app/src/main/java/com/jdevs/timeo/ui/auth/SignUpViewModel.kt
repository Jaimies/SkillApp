package com.jdevs.timeo.ui.auth

import com.jdevs.timeo.domain.usecase.auth.SignUpUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) :
    AuthViewModel() {

    val signUp = SingleLiveEvent<Any>()
    val navigateToSignIn = SingleLiveEvent<Any>()

    fun createAccount(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess) {

            signUpUseCase.invoke(email, password)
        }
    }

    fun triggerSignUp() {

        signUp.call()
        hideKeyboard()
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
