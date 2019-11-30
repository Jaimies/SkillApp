package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class LoaderViewModel : ViewModel() {
    val isLoading get() = _isLoading as LiveData<Boolean>

    private val _isLoading = MutableLiveData(false)

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
