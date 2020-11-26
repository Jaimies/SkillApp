package com.maxpoliakov.skillapp.ui.activitydetail

import com.maxpoliakov.skillapp.model.Recordable
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableState
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewModel

class SubActivityViewModel : RecordableViewModel<RecordableState, Recordable>() {
    override fun createState(item: Recordable) = RecordableState(item)
}
