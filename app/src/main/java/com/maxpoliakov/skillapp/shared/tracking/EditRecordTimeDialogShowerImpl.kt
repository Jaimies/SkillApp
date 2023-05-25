package com.maxpoliakov.skillapp.shared.tracking

import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.RecordChange
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
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
