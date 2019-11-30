package com.jdevs.timeo.viewmodel.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ItemListViewModel : ViewModel() {

    private val _isEmpty = MutableLiveData(true)
    private val _isLoaded = MutableLiveData(false)

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    val isLoaded: LiveData<Boolean> get() = _isLoaded

    fun onLoaded() {

        _isLoaded.value = true
    }

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }

    abstract fun onFragmentDestroyed()

    interface Navigator {
        fun onLastItemReached()
    }
}
