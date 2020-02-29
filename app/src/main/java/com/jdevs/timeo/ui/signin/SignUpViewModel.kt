package com.jdevs.timeo.ui.signin

import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
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

            authRepository.createAccount(email, password)
        }
    }

    fun triggerSignUp() {

        signUp.call()
        hideKeyboard()
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
