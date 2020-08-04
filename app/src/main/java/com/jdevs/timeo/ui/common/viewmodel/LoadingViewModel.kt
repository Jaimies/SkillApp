package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.util.lifecycle.launchCoroutine

open class LoadingViewModel(loadingByDefault: Boolean = false) : KeyboardHidingViewModel() {
    val isLoading get() = _isLoading as LiveData<Boolean>
    private val _isLoading = MutableLiveData(loadingByDefault)

    fun showLoader() {
        _isLoading.value = true
    }

    fun hideLoader() {
        _isLoading.value = false
    }

    inline fun runWithLoader(crossinline block: suspend () -> Boolean) {
        launchCoroutine {
            showLoader()
            val isSuccessful = block()
            if (!isSuccessful) hideLoader()
        }
    }
}
