package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.jdevs.timeo.model.OperationItem
import com.jdevs.timeo.model.ViewItem

abstract class ListViewModel<T : ViewItem> : LoaderViewModel(isLoadingByDefault = true) {

    abstract val localLiveData: LiveData<out PagedList<out T>>
    abstract val remoteLiveDatas: List<LiveData<out OperationItem<out T>>>

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }
}
