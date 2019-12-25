package com.jdevs.timeo.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.common.viewmodel.LoaderViewModel

abstract class AuthViewModel : LoaderViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>
    val isContentHidden get() = _isContentHidden as LiveData<Boolean>

    private val _emailError = MutableLiveData("")
    private val _passwordError = MutableLiveData("")
    private val _isContentHidden = MutableLiveData(false)

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
