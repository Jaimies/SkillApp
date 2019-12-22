package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity

interface ActivitiesDataSource {

    val activities: LiveData<List<Activity>>
    suspend fun addActivity(activity: Activity)
}