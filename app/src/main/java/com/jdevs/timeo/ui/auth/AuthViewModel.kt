package com.jdevs.timeo.ui.auth

import androidx.annotation.StringRes
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.R.string.email_empty
import com.jdevs.timeo.R.string.email_invalid
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.ui.common.viewmodel.LoadingViewModel

abstract class AuthViewModel : LoadingViewModel() {

    val snackbar: LiveData<Int> get() = _snackbar
    private val _snackbar = SingleLiveEvent<Int>()

    val navigateToOverview: LiveData<Any> get() = _navigateToOverview
    private val _navigateToOverview = SingleLiveEvent<Any>()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emailError get() = _emailError as LiveData<Int>
    private val _emailError = MutableLiveData(-1)
    val passwordError get() = _passwordError as LiveData<Int>
    private val _passwordError = MutableLiveData(-1)

    protected fun checkEmail(email: String): Boolean {
        if (email.isBlank()) setEmailError(email_empty)
        else if (!EMAIL_ADDRESS.matcher(email).matches()) setEmailError(
            email_invalid
        )
        else {
            setEmailError(-1)
            return true
        }

        return false
    }

    protected fun setEmailError(@StringRes resId: Int) {
        _emailError.value = resId
    }

    protected fun setPasswordError(@StringRes resId: Int) {
        _passwordError.value = resId
    }

    protected fun snackbar(@StringRes resId: Int) = _snackbar.setValue(resId)
    protected fun navigateToOverview() = _navigateToOverview.call()
}
