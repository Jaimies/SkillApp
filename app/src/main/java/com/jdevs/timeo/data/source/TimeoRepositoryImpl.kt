package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.remote.RemoteDataSource
import javax.inject.Inject

class TimeoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: TimeoDataSource,
    private val authRepository: AuthRepository
) : TimeoRepository {

    private val isUserSignedIn
        get() = authRepository.isUserSignedIn

    private val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val activities get() = currentDataSource.activities
    override val records get() = currentDataSource.records
    override val dayStats get() = currentDataSource.dayStats
    override val weekStats get() = currentDataSource.weekStats
    override val monthStats get() = currentDataSource.monthStats

    override fun getActivityById(id: Int, documentId: String) =
        currentDataSource.getActivityById(id, documentId)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) = currentDataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)

    override suspend fun addRecord(record: Record) = currentDataSource.addRecord(record)

    override suspend fun deleteRecord(record: Record) = currentDataSource.deleteRecord(record)

    private fun performOnRemote(action: (RemoteDataSource) -> Unit) {

        if (isUserSignedIn) {

            action(remoteDataSource)
        }
    }

    override fun resetActivitiesMonitor() = performOnRemote { it.resetActivitiesMonitor() }

    override fun resetRecordsMonitor() = performOnRemote { it.resetRecordsMonitor() }

    override fun resetDayStatsMonitor() = performOnRemote { it.resetDayStatsMonitor() }

    override fun resetWeekStatsMonitor() = performOnRemote { it.resetWeekStatsMonitor() }

    override fun resetMonthStatsMonitor() = performOnRemote { it.resetMonthStatsMonitor() }
}
