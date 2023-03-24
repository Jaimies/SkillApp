package com.maxpoliakov.skillapp.shared.tracking

import com.maxpoliakov.skillapp.domain.model.Record

interface SnackbarLabelFormatter {
    fun getLabel(records: List<Record>): String
}
