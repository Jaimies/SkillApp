package com.jdevs.timeo.ui.activitydetail

import com.jdevs.timeo.model.ActivityMinimalItem
import com.jdevs.timeo.ui.common.recordable.RecordableViewModel
import com.jdevs.timeo.util.time.getFriendlyHours

class ActivityState(activity: ActivityMinimalItem) {

    val name = activity.name
    val totalTime = getFriendlyHours(activity.totalTime)
}

class SubActivityViewModel : RecordableViewModel<ActivityState, ActivityMinimalItem>() {

    override fun createState(item: ActivityMinimalItem) = ActivityState(item)
}
