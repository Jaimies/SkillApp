package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesDataSource

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    override val activities: ItemsLiveData?

    override suspend fun saveActivity(activity: Activity): WriteBatch

    fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetActivitiesMonitor()
}
