package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.source.ItemListRepository

abstract class ItemListViewModel : LoaderViewModel(true) {

    val isEmpty: LiveData<Boolean> get() = _isEmpty

    protected abstract val repository: ItemListRepository
    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }

    abstract fun onFragmentDestroyed()

    interface Navigator {

        fun onLastItemReached()
    }
}
