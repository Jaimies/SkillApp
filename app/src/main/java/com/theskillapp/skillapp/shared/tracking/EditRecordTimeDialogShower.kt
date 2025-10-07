package com.theskillapp.skillapp.shared.tracking

import com.theskillapp.skillapp.domain.model.Record

interface EditRecordTimeDialogShower {
    fun show(record: Record)
}
