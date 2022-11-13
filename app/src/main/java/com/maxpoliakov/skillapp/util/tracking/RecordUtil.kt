package com.maxpoliakov.skillapp.util.tracking

import com.maxpoliakov.skillapp.domain.model.Record

interface RecordUtil {
    fun notifyRecordAdded(record: Record)
}
