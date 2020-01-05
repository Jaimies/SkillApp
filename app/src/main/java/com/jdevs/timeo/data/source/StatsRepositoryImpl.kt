package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.source.remote.StatsRemoteDataSource
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    remoteDataSource: StatsRemoteDataSource,
    localDataSource: StatsDataSource,
    authRepository: AuthRepository
) : BaseRepository<StatsRemoteDataSource, StatsDataSource>(
    remoteDataSource, localDataSource, authRepository
), StatsRepository {

    override val dayStats get() = currentDataSource.dayStats
    override val weekStats get() = currentDataSource.weekStats
    override val monthStats get() = currentDataSource.monthStats

    override suspend fun updateStats(date: OffsetDateTime, time: Long) =
        performOnRemoteSuspend { it.updateStats(date, time) }

    override fun resetDayStatsMonitor() = performOnRemote { it.resetDayStatsMonitor() }

    override fun resetWeekStatsMonitor() = performOnRemote { it.resetWeekStatsMonitor() }

    override fun resetMonthStatsMonitor() = performOnRemote { it.resetMonthStatsMonitor() }
}
