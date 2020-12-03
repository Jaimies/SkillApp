package com.maxpoliakov.skillapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordDateUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class RecordViewModel(
    private val changeRecordDate: ChangeRecordDateUseCase,
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
    }

    fun changeRecordDate(newDate: LocalDate) {
        ioScope.launch {
            changeRecordDate.run(record.value!!.id, newDate)
        }
    }

    fun showMenu() = showMenu.call()

    class Factory @Inject constructor(
        private val changeRecordDate: ChangeRecordDateUseCase,
        private val deleteRecord: DeleteRecordUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create() = RecordViewModel(changeRecordDate, deleteRecord, ioScope)
    }
}
