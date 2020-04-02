package com.jdevs.timeo.ui.common.recordable

import com.jdevs.timeo.model.Recordable
import com.jdevs.timeo.util.time.getFriendlyHours

open class RecordableState(recordable: Recordable) {
    val name = recordable.name
    val totalTime = getFriendlyHours(recordable.totalTime)
}
