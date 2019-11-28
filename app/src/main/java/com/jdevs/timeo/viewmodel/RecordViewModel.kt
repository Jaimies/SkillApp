package com.jdevs.timeo.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.util.getFriendlyTime

class RecordViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    private val _time = MutableLiveData("")

    var navigator: Navigator? = null

    val name: LiveData<String> get() = _name
    val time: LiveData<String> get() = _time

    fun setRecord(record: Record) {
        _name.value = record.name
        _time.value = record.time.getFriendlyTime()
    }

    interface Navigator {
        fun deleteRecord(view: View): Boolean
    }
}
