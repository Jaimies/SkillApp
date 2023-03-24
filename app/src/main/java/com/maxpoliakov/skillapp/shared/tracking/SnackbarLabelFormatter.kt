package com.maxpoliakov.skillapp.shared.tracking

import java.time.Duration

interface SnackbarLabelFormatter {
    fun getLabel(duration: Duration): String
}
