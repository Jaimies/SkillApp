package com.maxpoliakov.skillapp.ui.history.recyclerview.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Change
import com.maxpoliakov.skillapp.domain.model.RangeChange
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.RecordChange
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
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
