package com.maxpoliakov.skillapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class RecordViewModel @Inject constructor(
    private val editRecord: EditRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val ioScope: CoroutineScope
) {
    val showMenu = SingleLiveEvent<Any>()
    val record: LiveData<Record> get() = _record
    private val _record = MutableLiveData<Record>()

    fun setRecord(record: Record) {
        _record.value = record
    }

    fun deleteRecord() {
        ioScope.launch {
            deleteRecord.run(record.value!!.id)
        }
        logEvent("delete_record")
    }

    fun changeRecordDate(newDate: LocalDate) {
        ioScope.launch {
            editRecord.changeDate(record.value!!.id, newDate)
        }
        logEvent("change_record_date")
    }

    fun changeRecordTime(newCount: Long) {
        ioScope.launch {
            editRecord.changeCount(record.value!!.id, newCount)
        }
        logEvent("change_record_time")
    }

    fun showMenu(): Boolean {
        showMenu.call()
        return false
    }
}
