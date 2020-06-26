package com.jdevs.timeo.ui.auth

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.R
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.ui.common.viewmodel.LoadingViewModel
import com.jdevs.timeo.util.isValidEmail

abstract class AuthViewModel : LoadingViewModel() {

    val snackbar: LiveData<Int> get() = _snackbar
    protected val _snackbar = SingleLiveEvent<@StringRes Int>()

    val navigateToOverview: LiveData<Any> get() = _navigateToOverview
    protected val _navigateToOverview = SingleLiveEvent<Any>()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emailError = MutableLiveData(-1)
    val passwordError = MutableLiveData(-1)

    protected fun checkEmail(email: String): Boolean {

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
}
