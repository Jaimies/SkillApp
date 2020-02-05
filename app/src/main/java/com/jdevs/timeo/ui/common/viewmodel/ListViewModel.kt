package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.jdevs.timeo.domain.model.Operation

abstract class ListViewModel : LoaderViewModel(isLoadingByDefault = true) {

    abstract val localLiveData: LiveData<out PagedList<*>>
    abstract val remoteLiveDatas: List<LiveData<Operation>>

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {

        _isEmpty.value = length == 0
    }
}
