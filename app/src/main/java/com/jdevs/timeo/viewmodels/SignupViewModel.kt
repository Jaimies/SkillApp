package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignupViewModel : AuthViewModel() {
    private val _emailError = MutableLiveData("")
    private val _passwordError = MutableLiveData("")

    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>

    var navigator: Navigator? = null

    fun setEmailError(error: String) {
        _passwordError.value = ""
        _emailError.value = error
    }

    fun setPasswordError(error: String) {
        _emailError.value = ""
        _passwordError.value = error
    }

    interface Navigator : AuthViewModel.Navigator {
        fun navigateToLogin()
        fun signup(email: String, password: String)
    }
}
