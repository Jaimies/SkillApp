package com.maxpoliakov.skillapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record

class RecordViewModel {
    val record: LiveData<Record> get() = _record
    private val _record = MutableLiveData<Record>()

    fun setRecord(record: Record) {
        _record.value = record
    }
}
