package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultStatsRepository @Inject constructor(
    private val remoteDataSource: StatsRemoteDataSource,
    private val localDataSource: StatsLocalDataSource,
    private val authRepository: AuthRepository
) : StatsRepository {

    override val dayStats get() = localDataSource.dayStats
    override val weekStats get() = localDataSource.weekStats
    override val monthStats get() = localDataSource.monthStats
    override val dayStatsRemote get() = remoteDataSource.dayStats
    override val weekStatsRemote get() = remoteDataSource.weekStats
    override val monthStatsRemote get() = remoteDataSource.monthStats

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {

        if (authRepository.isUserSignedIn) {

            remoteDataSource.updateStats(date, time)
        }
    }
}
