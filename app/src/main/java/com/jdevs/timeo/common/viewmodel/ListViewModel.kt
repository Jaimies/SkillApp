package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.ItemsLiveData
import com.jdevs.timeo.util.SingleLiveEvent

abstract class ListViewModel : LoaderViewModel() {

    val onLastItemReached = SingleLiveEvent<Any>()
    val isEmpty: LiveData<Boolean> get() = _isEmpty
    abstract val liveData: ItemsLiveData?
    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }
}
