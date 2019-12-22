package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultActivitiesRepository(
    private val remoteDataSource: ActivitiesDataSource,
    private val localDataSource: ActivitiesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ActivitiesRepository {

    private val isUserSignedIn get() = AuthRepository.isUserSignedIn

    override suspend fun addActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.addActivity(activity)

        } else {

            localDataSource.addActivity(activity)
        }
    }

    override fun getActivities(): LiveData<List<Activity>> = localDataSource.activities
}