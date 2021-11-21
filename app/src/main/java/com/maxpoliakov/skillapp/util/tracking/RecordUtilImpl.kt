package com.maxpoliakov.skillapp.util.tracking

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.ChangeRecordTimeUseCase
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordUtilImpl @Inject constructor(
    private val changeRecordTime: ChangeRecordTimeUseCase,
    private val ioScope: CoroutineScope
) : RecordUtil {

    override fun notifyRecordAdded(view: View, record: Record) {
        val snackbar = Snackbar.make(view, R.string.record_added, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.change_time) { editTime(view, record) }
        snackbar.setActionTextColor(view.context.getColorAttributeValue(R.attr.snackbarActionTextColor))
        snackbar.show()
    }

    private fun editTime(view: View, record: Record) {
        view.context.showTimePicker(record.time) { newTime ->
            ioScope.launch {
                changeRecordTime.run(record.id, newTime)
            }
        }
    }
}
