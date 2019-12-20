package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.util.SingleLiveEvent

open class LoaderViewModel(isLoadingByDefault: Boolean = false) : ViewModel() {

    val isLoading get() = _isLoading as LiveData<Boolean>
    val hideKeyboard = SingleLiveEvent<Any>()

    private val _isLoading = MutableLiveData(isLoadingByDefault)

    fun showLoader() {

        _isLoading.value = true
    }

    fun hideLoader() {

        _isLoading.value = false
    }
}
