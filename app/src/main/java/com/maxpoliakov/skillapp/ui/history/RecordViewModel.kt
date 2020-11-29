package com.maxpoliakov.skillapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.model.RecordItem
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.time.getFriendlyTime

class RecordViewModel {

    val showDeleteDialog = SingleLiveEvent<Any>()
    val state: LiveData<RecordState> get() = _state
    private val _state = MutableLiveData<RecordState>()

    class RecordState(record: RecordItem) {
        val name = record.name
        val time = getFriendlyTime(record.time)
    }

    fun setRecord(record: RecordItem) {

        _state.value = RecordState(record)
    }

    fun showDeleteDialog(): Boolean {

        showDeleteDialog.call()
        return false
    }
}
