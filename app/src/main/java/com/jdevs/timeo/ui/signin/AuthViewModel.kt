package com.jdevs.timeo.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.ui.common.viewmodel.LoadingViewModel

abstract class AuthViewModel : LoadingViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>
    private val _emailError = MutableLiveData<String>()
    private val _passwordError = MutableLiveData<String>()

    fun setEmailError(error: String) {

        _passwordError.value = ""
        _emailError.value = error
    }

    fun setPasswordError(error: String) {

        _emailError.value = ""
        _passwordError.value = error
    }
}
