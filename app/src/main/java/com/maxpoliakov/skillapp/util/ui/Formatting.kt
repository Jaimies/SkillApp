@file:JvmName("FormattingUtil")

package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.ui.activities.ActivityState

fun ActivityState?.getSubactivityLabel(context: Context) = when {
    this == null -> null
    subActivitiesCount > 0 -> context.resources.getQuantityString(
        R.plurals.subactivities_count, subActivitiesCount, subActivitiesCount
    )
    parentActivity != null -> context.getString(R.string.subactivity_of, parentActivity.name)
    else -> ""
}
