package com.theskillapp.skillapp.shared.tracking

import java.time.Duration

interface RecordAddedSnackbarLabelFormatter {
    fun getLabel(duration: Duration): String
}
