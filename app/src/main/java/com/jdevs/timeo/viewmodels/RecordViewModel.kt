package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.navigators.RecordNavigator
import com.jdevs.timeo.util.Time

class RecordViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    private val _time = MutableLiveData("")

    var navigator: RecordNavigator? = null

    val name: LiveData<String> get() = _name
    val time: LiveData<String> get() = _time

    fun setRecord(record: Record) {
        _name.value = record.name
        _time.value = Time.minsToTime(record.time)
    }
}