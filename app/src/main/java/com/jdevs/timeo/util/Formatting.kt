@file:JvmName("FormattingUtil")

package com.jdevs.timeo.util

import android.content.Context
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.activities.ActivityState

fun ActivityState?.getSubactivityLabel(context: Context) = when {
    this == null -> null
    hasSubActivities -> context.resources.getQuantityString(
        R.plurals.subactivities_count, subActivitiesCount, subActivitiesCount
    )
    hasParentActivity -> context.getString(R.string.subactivity_of, parentActivityName)
    else -> ""
}
