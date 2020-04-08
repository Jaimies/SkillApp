package com.jdevs.timeo.ui.auth

import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.usecase.auth.SignUpUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) :
    AuthViewModel() {

    val signUp = SingleLiveEvent<Any>()
    val navigateToSignIn = SingleLiveEvent<Any>()

    fun createAccount(
        email: String, password: String,
        onResult: (SignUpResult) -> Unit
    ) = launchSuspendingProcess(onResult, { result -> result == SignUpResult.Success }) {

        signUpUseCase(email, password)
    }

    fun signUp() = signUp.call()
    fun navigateToSignIn() = navigateToSignIn.call()
}
