package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower
import java.time.Duration
import javax.inject.Inject

class RecordAddedSnackbarShowerImpl @Inject constructor(
    private val snackbarShower: SnackbarShower,
    private val labelFormatter: RecordAddedSnackbarLabelFormatter,
    private val editRecordTimeDialogShower: EditRecordTimeDialogShower,
) : RecordAddedSnackbarShower {

    override fun showRecordAddedSnackbar(duration: Duration, recordToBeEdited: Record?) {
        snackbarShower.show(labelFormatter.getLabel(duration), recordToBeEdited?.let(this::createSnackbarAction))
    }

    private fun createSnackbarAction(record : Record): SnackbarShower.Action {
        return SnackbarShower.Action(
            stringResId = R.string.change_time,
            listener = { editRecordTimeDialogShower.show(record) },
        )
    }
}
