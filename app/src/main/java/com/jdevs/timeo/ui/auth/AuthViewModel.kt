package com.jdevs.timeo.ui.auth

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.ui.common.viewmodel.LoadingViewModel

abstract class AuthViewModel : LoadingViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
}
