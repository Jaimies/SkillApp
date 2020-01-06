package com.jdevs.timeo.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.model.Record
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.toFriendlyTime

class RecordViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val time: LiveData<String> get() = _time
    val showDeleteDialog = SingleLiveEvent<Any>()

    private val _name = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setRecord(record: Record) {

        _name.value = record.name
        _time.value = record.time.toFriendlyTime()
    }

    fun showDeleteDialog(): Boolean {

        showDeleteDialog.call()
        return false
    }
}
