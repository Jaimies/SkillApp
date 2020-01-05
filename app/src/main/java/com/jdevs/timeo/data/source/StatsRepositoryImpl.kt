package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.source.remote.StatsRemoteDataSource
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val remoteDataSource: StatsRemoteDataSource,
    private val localDataSource: TimeoDataSource,
    authRepository: AuthRepository
) : BaseRepository(authRepository), StatsRepository {

    private val currentDataSource get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val dayStats get() = currentDataSource.dayStats
    override val weekStats get() = currentDataSource.weekStats
    override val monthStats get() = currentDataSource.monthStats

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {

        if (isUserSignedIn) {

            remoteDataSource.updateStats(date, time)
        }
    }

    override fun resetDayStatsMonitor() = performOnRemote { it.resetDayStatsMonitor() }

    override fun resetWeekStatsMonitor() = performOnRemote { it.resetWeekStatsMonitor() }

    override fun resetMonthStatsMonitor() = performOnRemote { it.resetMonthStatsMonitor() }
}
