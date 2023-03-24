package com.maxpoliakov.skillapp.shared.tracking

import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.RecordChange
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower.Action
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class RecordUtilImpl @Inject constructor(
    private val editRecord: EditRecordUseCase,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val snackbarShower: SnackbarShower,
    private val labelFormatter: SnackbarLabelFormatter,
    private val fragmentManager: FragmentManager,
) : RecordUtil {

    override fun notifyRecordsAdded(records: List<Record>) {
        if (records.isEmpty()) return
        snackbarShower.show(labelFormatter.getLabel(records), getAction(records))
    }

    private fun getAction(records: List<Record>): Action? {
        if (records.size == 1) return getChangeTimeAction(records[0])
        return null
    }

    private fun getChangeTimeAction(record: Record): Action {
        return Action(R.string.change_time, { showChangeTimeDialog(record) })
    }

    private fun showChangeTimeDialog(record: Record) {
        UiMeasurementUnit.Millis.showPicker(fragmentManager, record.count, editMode = true) { newTime ->
            scope.launch {
                editRecord.change(record.id, RecordChange.Count(newTime))
            }
        }
    }
}
