package com.jdevs.timeo.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getFriendlyTime

class RecordViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val time: LiveData<String> get() = _time
    val showDeleteDialog = SingleLiveEvent<Any>()

    private val _name = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setRecord(record: RecordItem) {

        _name.value = record.name
        _time.value = getFriendlyTime(record.time)
    }

    fun showDeleteDialog(): Boolean {

        showDeleteDialog.call()
        return false
    }
}
