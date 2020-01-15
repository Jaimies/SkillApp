package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class LoaderViewModel(isLoadingByDefault: Boolean = false) : KeyboardHidingViewModel() {

    val isLoading get() = _isLoading as LiveData<Boolean>
    private val _isLoading = MutableLiveData(isLoadingByDefault)

    fun showLoader() {

        _isLoading.value = true
    }

    fun hideLoader() {

        _isLoading.value = false
    }
}
