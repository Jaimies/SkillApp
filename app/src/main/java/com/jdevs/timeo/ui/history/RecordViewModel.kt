package com.jdevs.timeo.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.util.time.getFriendlyTime

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
