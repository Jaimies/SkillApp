package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.domain.model.Record

interface EditRecordTimeDialogShower {
    fun show(record: Record)
}
