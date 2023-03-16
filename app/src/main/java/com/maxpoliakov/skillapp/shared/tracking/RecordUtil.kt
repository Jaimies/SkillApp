package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.domain.model.Record

interface RecordUtil {
    fun notifyRecordAdded(record: Record)
}
