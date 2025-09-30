package com.theskillapp.skillapp.ui.history.recyclerview.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.theskillapp.skillapp.domain.di.ApplicationScope
import com.theskillapp.skillapp.domain.model.Change
import com.theskillapp.skillapp.domain.model.RangeChange
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.RecordChange
import com.theskillapp.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.theskillapp.skillapp.domain.usecase.records.EditRecordUseCase
import com.theskillapp.skillapp.model.HistoryUiModel
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class RecordViewModel @Inject constructor(
    private val editRecord: EditRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    @ApplicationScope
    private val scope: CoroutineScope
) {
    val showMenu = SingleLiveEventWithoutData()
    val record: LiveData<HistoryUiModel.Record> get() = _record
    private val _record = MutableLiveData<HistoryUiModel.Record>()

    fun setRecord(record: HistoryUiModel.Record) {
        _record.value = record
    }

    fun deleteRecord() {
        scope.launch {
            deleteRecord.run(record.value!!.id)
        }
    }

    fun changeRecordDate(newDate: LocalDate) {
        change(RecordChange.Date(newDate))
    }

    fun changeRecordTime(newCount: Long) {
        change(RecordChange.Count(newCount))
    }

    fun changeStartTime(newTime: LocalTime) {
        change(RecordChange.TimeRange(RangeChange.Start(newTime)))
    }

    fun changeEndTime(newTime: LocalTime) {
        change(RecordChange.TimeRange(RangeChange.End(newTime)))
    }

    fun change(change: Change<Record>) {
        scope.launch {
            editRecord.change(record.value?.id ?: return@launch, change)
        }
    }

    fun showMenu(): Boolean {
        showMenu.call()
        return false
    }
}
