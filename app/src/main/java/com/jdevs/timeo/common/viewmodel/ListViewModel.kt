package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class ListViewModel : LoaderViewModel(true) {

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    private val _isEmpty = MutableLiveData(true)

    abstract fun onFragmentDestroyed()

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }

    interface Navigator {

        fun onLastItemReached()
    }
}
