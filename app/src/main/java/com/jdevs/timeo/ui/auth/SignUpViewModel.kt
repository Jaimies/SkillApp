package com.jdevs.timeo.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import com.jdevs.timeo.R.string.check_connection
import com.jdevs.timeo.R.string.email_invalid
import com.jdevs.timeo.R.string.enter_password
import com.jdevs.timeo.R.string.password_too_long
import com.jdevs.timeo.R.string.password_too_short
import com.jdevs.timeo.R.string.password_too_weak
import com.jdevs.timeo.R.string.try_again
import com.jdevs.timeo.R.string.user_already_exists
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.model.result.SignUpResult.InternalError
import com.jdevs.timeo.domain.model.result.SignUpResult.InvalidEmail
import com.jdevs.timeo.domain.model.result.SignUpResult.Success
import com.jdevs.timeo.domain.model.result.SignUpResult.UserAlreadyExists
import com.jdevs.timeo.domain.model.result.SignUpResult.WeakPassword
import com.jdevs.timeo.domain.usecase.auth.SignUpUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.util.hardware.NetworkUtils

private val passwordLength = 8..100

class SignUpViewModel @ViewModelInject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val networkUtils: NetworkUtils
) : AuthViewModel() {

    val navigateToSignIn = SingleLiveEvent<Any>()

    fun signUp() =
        performSignUp(email.value.orEmpty(), password.value.orEmpty())

    private fun performSignUp(email: String, password: String) {

        if (!checkEmail(email) and checkPassword(password)) return
        if (!networkUtils.hasNetworkConnection()) snackbar(check_connection)
        else runWithLoader {
            val result = signUpUseCase.run(email, password)
            handleSignUpResult(result)
            result == Success
        }
    }

    private fun checkPassword(password: String): Boolean {
        setPasswordError(
            when {
                password.isBlank() -> enter_password
                password.length < passwordLength.first -> password_too_short
                password.length > passwordLength.last -> password_too_long
                else -> {
                    setPasswordError(-1)
                    return true
                }
            }
        )

        return false
    }

    private fun handleSignUpResult(result: SignUpResult) = when (result) {
        Success -> navigateToOverview()
        InvalidEmail -> setEmailError(email_invalid)
        WeakPassword -> setPasswordError(password_too_weak)
        UserAlreadyExists -> setEmailError(user_already_exists)
        InternalError -> snackbar(try_again)
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
