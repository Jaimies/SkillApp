package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.repositories.FirebaseAuthRepository

open class AuthViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading as LiveData<Boolean>

    protected val authRepository = FirebaseAuthRepository()

    fun showLoader() {
        _isLoading.value = true
    }

    fun hideLoader() {
        _isLoading.value = false
    }

    interface Navigator {
        fun hideKeyboard()
    }
}
