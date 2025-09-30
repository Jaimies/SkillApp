package com.theskillapp.skillapp.shared.tracking

import androidx.fragment.app.FragmentManager
import com.theskillapp.skillapp.domain.di.ApplicationScope
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.RecordChange
import com.theskillapp.skillapp.domain.usecase.records.EditRecordUseCase
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditRecordTimeDialogShowerImpl @Inject constructor(
    private val fragmentManager: FragmentManager,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val editRecord: EditRecordUseCase,
): EditRecordTimeDialogShower {
    override fun show(record: Record) {
        record.unit.mapToUI().showPicker(fragmentManager, record.count, editMode = true) { newTime ->
            editRecord(record, newTime)
        }
    }

    private fun editRecord(record: Record, newTime: Long) {
        scope.launch {
            editRecord.change(record.id, RecordChange.Count(newTime))
        }
    }
}
