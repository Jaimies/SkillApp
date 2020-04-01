package com.jdevs.timeo.ui.common.recordable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SingleLiveEvent

abstract class RecordableViewModel<State : Any, Item : Any> {

    val state: LiveData<State> get() = _state
    private val _state = MutableLiveData<State>()

    abstract fun createState(item: Item): State

    fun setItem(item: Item) {
        _state.value = createState(item)
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
