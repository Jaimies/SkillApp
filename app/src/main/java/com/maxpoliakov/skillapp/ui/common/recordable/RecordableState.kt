package com.maxpoliakov.skillapp.ui.common.recordable

import com.maxpoliakov.skillapp.model.Recordable
import com.maxpoliakov.skillapp.util.time.getFriendlyHours

open class RecordableState(recordable: Recordable) {
    val name = recordable.name
    val totalTime = getFriendlyHours(recordable.totalTime)
}
