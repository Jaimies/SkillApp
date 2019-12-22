package com.jdevs.timeo.data.source

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

    override suspend fun saveActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.saveActivity(activity)
        } else {

            localDataSource.saveActivity(activity)
        }
    }

    override suspend fun deleteActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.deleteActivity(activity)
        } else {

            localDataSource.deleteActivity(activity)
        }
    }

    override fun getActivities() = localDataSource.activities
}