package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class LoaderViewModel(isLoadingByDefault: Boolean = false) : ViewModel() {

    val isLoading get() = _isLoading as LiveData<Boolean>

    private val _isLoading = MutableLiveData(isLoadingByDefault)

    open fun showLoader() {

        _isLoading.value = true
    }

    open fun hideLoader() {

        _isLoading.value = false
    }

    interface Navigator {
        fun hideKeyboard()
    }
}
