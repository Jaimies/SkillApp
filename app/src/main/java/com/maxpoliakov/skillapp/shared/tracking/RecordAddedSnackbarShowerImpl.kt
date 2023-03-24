package com.maxpoliakov.skillapp.shared.tracking

import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.RecordChange
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

class RecordAddedSnackbarShowerImpl @Inject constructor(
    private val snackbarShower: SnackbarShower,
    private val fragmentManager: FragmentManager,
    private val editRecord: EditRecordUseCase,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val labelFormatter: RecordAddedSnackbarLabelFormatter,
) : RecordAddedSnackbarShower {

    override fun showRecordAddedSnackbar(duration: Duration, recordToBeEdited: Record?) {
        snackbarShower.show(labelFormatter.getLabel(duration), recordToBeEdited?.let(this::createSnackbarAction))
    }

    private fun createSnackbarAction(record : Record): SnackbarShower.Action {
        return SnackbarShower.Action(R.string.change_time, { showChangeTimeDialog(record) })
    }

    private fun showChangeTimeDialog(record: Record) {
        UiMeasurementUnit.Millis.showPicker(fragmentManager, record.count, editMode = true) { newTime ->
            scope.launch {
                editRecord.change(record.id, RecordChange.Count(newTime))
            }
        }
    }
}
