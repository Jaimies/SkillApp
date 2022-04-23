package com.maxpoliakov.skillapp.util.tracking

import android.view.View
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

interface RecordUtil {
    fun notifyRecordAdded(view: View, record: Record, unit: UiMeasurementUnit)
}
