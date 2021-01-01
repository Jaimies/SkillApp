package com.maxpoliakov.skillapp.ui.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordDateUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.DeleteRecordUseCase
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.time.toReadableTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

class RecordViewModel(
    private val changeRecordDate: ChangeRecordDateUseCase,
    private val changeRecordTime: ChangeRecordTimeUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val ioScope: CoroutineScope,
    private val context: Context
) {
    val showMenu = SingleLiveEvent<Any>()
    val record: LiveData<Record> get() = _record
    private val _record = MutableLiveData<Record>()

    val time = record.map {
        it.time.toReadableTime(context)
    }

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

    fun changeRecordTime(newTime: Duration) {
        ioScope.launch {
            changeRecordTime.run(record.value!!.id, newTime)
        }
    }

    fun showMenu() = showMenu.call()

    class Factory @Inject constructor(
        private val changeRecordDate: ChangeRecordDateUseCase,
        private val changeRecordTime: ChangeRecordTimeUseCase,
        private val deleteRecord: DeleteRecordUseCase,
        private val ioScope: CoroutineScope,
        @ApplicationContext
        private val context: Context
    ) {
        fun create() = RecordViewModel(
            changeRecordDate,
            changeRecordTime,
            deleteRecord,
            ioScope,
            context
        )
    }
}
