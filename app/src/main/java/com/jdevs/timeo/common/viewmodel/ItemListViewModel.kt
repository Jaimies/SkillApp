package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class ItemListViewModel : LoaderViewModel(true) {

    val isEmpty: LiveData<Boolean> get() = _isEmpty

    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }

    abstract fun onFragmentDestroyed()

    interface Navigator {

        fun onLastItemReached()
    }
}
