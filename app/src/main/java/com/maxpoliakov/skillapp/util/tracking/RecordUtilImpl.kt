package com.maxpoliakov.skillapp.util.tracking

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.records.EditRecordUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@ActivityScoped
class RecordUtilImpl @Inject constructor(
    private val editRecord: EditRecordUseCase,
    private val ioScope: CoroutineScope,
    private val activity: Activity,
) : RecordUtil {

    override fun notifyRecordAdded(record: Record) {
        val view = activity.findViewById<CoordinatorLayout>(R.id.snackbar_root)

        Snackbar.make(view, getLabel(view.context, record), Snackbar.LENGTH_LONG)
            .setAction(R.string.change_time) { editTime(view, record) }
            .setActionTextColor(view.context.getColorAttributeValue(R.attr.snackbarActionTextColor))
            .show()
    }

    private fun getLabel(context: Context, record: Record): String {
        val time = Duration.ofMillis(record.count)

        if (time.toHours() == 0L) {
            if (time.toMinutes() == 0L) {
                return context.getString(R.string.record_added, time.seconds)
            }

            return context.getString(R.string.record_added_with_minutes, time.toMinutes())
        }

        return context.getString(
            R.string.record_added_with_hours,
            time.toHours(),
            time.toMinutesPartCompat()
        )
    }

    private fun editTime(view: View, record: Record) {
        UiMeasurementUnit.Millis.showPicker(view.context, record.count, editMode = true) { newTime ->
            ioScope.launch {
                editRecord.changeCount(record.id, newTime)
            }
        }
    }
}
