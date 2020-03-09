package com.jdevs.timeo.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.ui.common.viewmodel.LoaderViewModel

abstract class AuthViewModel : LoaderViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>
    val isContentHidden get() = _isContentHidden as LiveData<Boolean>
    private val _isContentHidden = MutableLiveData(false)
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

    fun hideContent() {

        _isContentHidden.value = true
    }
}
