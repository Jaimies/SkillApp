@file:JvmName("FormattingUtil")

package com.jdevs.timeo.util.ui

import android.content.Context
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.activities.ActivityState

fun ActivityState?.getSubactivityLabel(context: Context) = when {
    this == null -> null
    subActivitiesCount > 0 -> context.resources.getQuantityString(
        R.plurals.subactivities_count, subActivitiesCount, subActivitiesCount
    )
    parentActivity != null -> context.getString(R.string.subactivity_of, parentActivity.name)
    else -> ""
}
