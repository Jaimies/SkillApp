package com.jdevs.timeo.ui.auth

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.jdevs.timeo.R
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.usecase.auth.SignUpUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.util.PASSWORD_MAX_LENGTH
import com.jdevs.timeo.util.PASSWORD_MIN_LENGTH
import com.jdevs.timeo.util.hardware.NetworkUtils
import com.jdevs.timeo.util.isValidEmail
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val networkUtils: NetworkUtils
) : AuthViewModel() {

    val snackbar: LiveData<Int> get() = _snackbar
    private val _snackbar = SingleLiveEvent<@StringRes Int>()

    val navigateToOverview: LiveData<Any> get() = _navigateToOverview
    private val _navigateToOverview = SingleLiveEvent<Any>()

    val navigateToSignIn = SingleLiveEvent<Any>()

    fun signUp() {
        val email = email.value.orEmpty()
        val password = password.value.orEmpty()

        when {
            !(checkEmail(email) and checkPassword(password)) -> return
            !networkUtils.hasNetworkConnection() -> _snackbar.value = R.string.check_connection
            else -> launchSuspendingProcess(::onSignUpResult, { it == SignUpResult.Success }) {
                signUpUseCase(email, password)
            }
        }
    }

    private fun checkPassword(password: String): Boolean {
        passwordError.value = when {
            password.isBlank() -> R.string.enter_password
            password.length < PASSWORD_MIN_LENGTH -> R.string.password_too_short
            password.length > PASSWORD_MAX_LENGTH -> R.string.password_too_long
            else -> {
                passwordError.value = -1
                return true
            }
        }

        return false
    }

    private fun checkEmail(email: String): Boolean {

        emailError.value = when {
            email.isBlank() -> R.string.email_empty
            !isValidEmail(email) -> R.string.email_invalid
            else -> {
                emailError.value = -1
                return true
            }
        }

        return false
    }

    private fun onSignUpResult(result: SignUpResult) = when (result) {
        SignUpResult.Success -> _navigateToOverview.call()
        SignUpResult.InvalidEmail -> emailError.value = R.string.email_invalid
        SignUpResult.WeakPassword -> passwordError.value = R.string.password_too_weak
        SignUpResult.UserAlreadyExists -> emailError.value = R.string.user_already_exists
        SignUpResult.InternalError -> _snackbar.value = R.string.try_again
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
