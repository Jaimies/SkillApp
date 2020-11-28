package com.maxpoliakov.skillapp.ui.activities

import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableState
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewModel

open class ActivityState(activity: ActivityItem) : RecordableState(activity)

class ActivityViewModel : RecordableViewModel<RecordableState, ActivityItem>() {
    override fun createState(item: ActivityItem) = RecordableState(item)
}
