package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jdevs.timeo.model.ViewItem

abstract class ListViewModel<T : ViewItem> : ViewModel() {
    abstract val liveData: LiveData<PagedList<T>>

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    private val _isEmpty = MutableLiveData(false)

    fun setLength(length: Int) {
        _isEmpty.value = length <= 0
    }
}
