package com.maxpoliakov.skillapp.util.tracking

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordUtilImpl @Inject constructor(
    private val changeRecordTime: ChangeRecordTimeUseCase,
    private val ioScope: CoroutineScope
) : RecordUtil {

    override fun notifyRecordAdded(view: View, record: Record, unit: UiMeasurementUnit) {
        val snackbar = Snackbar.make(view, "Record added but pls change me", Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.change_time) { editTime(view, record, unit) }
        snackbar.setActionTextColor(view.context.getColorAttributeValue(R.attr.snackbarActionTextColor))
        snackbar.show()
    }

    private fun editTime(view: View, record: Record, unit: UiMeasurementUnit) {
        unit.showPicker(view.context, record.count) { newTime ->
            ioScope.launch {
                changeRecordTime.run(record.id, newTime)
            }
        }
    }
}
