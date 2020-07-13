package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.model.ViewItem

abstract class ListViewModel<T : ViewItem> : LoadingViewModel(isLoadingByDefault = true) {

    abstract val localLiveData: LiveData<PagedList<T>>
    abstract fun getRemoteLiveDatas(fetchNewItems: Boolean): List<LiveData<Operation<T>>>

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    private val _isEmpty = MutableLiveData(true)

    fun setLength(length: Int) {
        hideLoader()
        _isEmpty.value = length <= 0
    }
}
