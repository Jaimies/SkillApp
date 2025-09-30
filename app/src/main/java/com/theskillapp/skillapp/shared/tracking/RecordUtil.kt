package com.theskillapp.skillapp.shared.tracking

import com.theskillapp.skillapp.domain.model.Record

interface RecordUtil {
    fun notifyRecordsAdded(records: List<Record>)
}
