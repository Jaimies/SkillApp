package com.maxpoliakov.skillapp.ui.common.recordable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.model.Recordable
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

abstract class RecordableViewModel<State : RecordableState, Item : Recordable> {
    val state: LiveData<State> get() = _state
    private val _state = MutableLiveData<State>()

    protected abstract fun createState(item: Item): State

    fun setItem(item: Item) {
        _state.value = createState(item)
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
}
