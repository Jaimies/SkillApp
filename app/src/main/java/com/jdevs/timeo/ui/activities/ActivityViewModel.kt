package com.jdevs.timeo.ui.activities

import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.ui.common.recordable.RecordableViewModel
import com.jdevs.timeo.util.time.getFriendlyHours

open class ActivityState(activity: ActivityItem) {

    val name = activity.name
    val totalTime = getFriendlyHours(activity.totalTime)

    val parentActivity = activity.parentActivity
    val parentActivityTime = parentActivity?.totalTime?.let { getFriendlyHours(it) }
    val subActivitiesCount = activity.subActivities.size
}

class ActivityViewModel : RecordableViewModel<ActivityState, ActivityItem>() {

    override fun createState(item: ActivityItem) = ActivityState(item)
}
