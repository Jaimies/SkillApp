package com.jdevs.timeo.ui.activitydetail

import com.jdevs.timeo.model.Recordable
import com.jdevs.timeo.ui.common.recordable.RecordableState
import com.jdevs.timeo.ui.common.recordable.RecordableViewModel

class SubActivityViewModel : RecordableViewModel<RecordableState, Recordable>() {

    override fun createState(item: Recordable) = RecordableState(item)
}
