package com.maxpoliakov.skillapp.ui.activities

import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableState
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewModel
import com.maxpoliakov.skillapp.util.time.getFriendlyHours

open class ActivityState(activity: ActivityItem) : RecordableState(activity) {
    val parentActivity = activity.parentActivity
    val parentActivityTime = parentActivity?.totalTime?.let { getFriendlyHours(it) }
    val subActivitiesCount = activity.subActivities.size
}

class ActivityViewModel : RecordableViewModel<ActivityState, ActivityItem>() {

    override fun createState(item: ActivityItem) = ActivityState(item)
}
