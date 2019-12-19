package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.util.SingleLiveEvent

@Suppress("VariableNaming", "PropertyName", "UNCHECKED_CAST")
abstract class ListViewModel : LoaderViewModel(true) {

    val onLastItemReached = SingleLiveEvent<Any>()
    val isEmpty: LiveData<Boolean> get() = _isEmpty

    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }
}
